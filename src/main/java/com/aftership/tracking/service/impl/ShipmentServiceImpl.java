package com.aftership.tracking.service.impl;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aftership.tracking.entity.ShipmentDetails;
import com.aftership.tracking.enums.CourierCodeEnum;
import com.aftership.tracking.exception.ShipmentTrackingException;
import com.aftership.tracking.model.NewTracking;
import com.aftership.tracking.model.NewTrackingRequest;
import com.aftership.tracking.model.ShipmentModel;
import com.aftership.tracking.repository.ShipmentDetailsRepository;
import com.aftership.tracking.restutil.RestApiClient;
import com.aftership.tracking.service.ShipmentService;

@Service
public class ShipmentServiceImpl implements ShipmentService {

    private static final Logger logger = LoggerFactory.getLogger(ShipmentServiceImpl.class);

    @Autowired
    private RestApiClient restApiClient;

    @Autowired
    private ShipmentDetailsRepository shipmentDetailsRepository;

    /**
     * @param requestModel
     * @return requestModel
     * @throws ShipmentTrackingException
     *
     * 1. Method validates shipment model object
     * 2. Checks the record already exists in h2 db - tracking number and courier code
     *      - if exists -> throw an exception and exist
     * 3. call aftership post api to create shipment tracking
     * 4. save shipment details in shipment_details table in h2 db
     * 5. return requestModel
     *
     */
    @Override
    public ShipmentModel createTracking(ShipmentModel requestModel) {
        logger.debug("In ShipmentServiceImpl :: called createTracking method for tracking id : {} and courier code : {} ", requestModel.getTrackingNumber(), requestModel.getCourierCode());
        validateShipmentModel(requestModel);
        restApiClient.makePostRestCall(new NewTrackingRequest(transformToNewTracking(requestModel)));
        ShipmentDetails entity = shipmentDetailsRepository.save(buildShipmentDetails(requestModel));
        requestModel.setId(entity.getId());
        logger.debug("Successfully created for tracking id : {} and courier code : {} ", requestModel.getTrackingNumber(), requestModel.getCourierCode());
        return requestModel;
    }

    private void validateShipmentModel(ShipmentModel requestModel) {
        logger.debug("Validating requestModel for tracking id : {} and courier code : {} ", requestModel.getTrackingNumber(), requestModel.getCourierCode());
        if (StringUtils.isEmpty(requestModel.getTrackingNumber())) {
            throw new ShipmentTrackingException("Please provide valid tracking number");
        } else if (StringUtils.isEmpty(requestModel.getOrigin())) {
            throw new ShipmentTrackingException("Please provide valid origin information");
        } else if (StringUtils.isEmpty(requestModel.getDestination())) {
            throw new ShipmentTrackingException("Please provide valid destination information");
        } else if (!CourierCodeEnum.getValues().contains(requestModel.getCourierCode())) {
            throw new ShipmentTrackingException("Please provide valid courier code");
        }
        boolean isShipmentAlreadyExists = shipmentDetailsRepository.existsByTrackingNumberAndCourierCode(requestModel.getTrackingNumber(), requestModel.getCourierCode());
        if(isShipmentAlreadyExists) {
            logger.error("Failed to created for tracking id : {} and courier code : {} as the record already exists", requestModel.getTrackingNumber(), requestModel.getCourierCode());
            throw new ShipmentTrackingException("Tracking number already exists.");
        }
        logger.debug("Validated requestModel successfully for tracking id : {} and courier code : {} ", requestModel.getTrackingNumber(), requestModel.getCourierCode());
    }

    public NewTracking transformToNewTracking(ShipmentModel requestModel) {
        NewTracking newTracking = new NewTracking();
        newTracking.setSlug(new String[]{requestModel.getCourierCode()});
        newTracking.setTrackingNumber(requestModel.getTrackingNumber());
        newTracking.setDestinationCountryIso3(getCountryCode(requestModel.getDestination()));
        newTracking.setOriginCountryIso3(getCountryCode(requestModel.getOrigin()));
        return newTracking;
    }

    private ShipmentDetails buildShipmentDetails(ShipmentModel requestModel) {
        ShipmentDetails shipmentDetails = new ShipmentDetails();
        shipmentDetails.setId(UUID.randomUUID().toString());
        shipmentDetails.setCourierCode(requestModel.getCourierCode());
        shipmentDetails.setTrackingNumber(requestModel.getTrackingNumber());
        shipmentDetails.setOrigin(requestModel.getOrigin());
        shipmentDetails.setDestination(requestModel.getDestination());
        return shipmentDetails;
    }

    private static String getCountryCode(String address) {
        String[] addressSplit = address.split(",");
        return addressSplit[addressSplit.length - 1].replaceAll("\\d", "").trim();
    }

}
