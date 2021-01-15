package com.aftership.tracking.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.aftership.tracking.entity.ShipmentDetails;
import com.aftership.tracking.enums.CourierCodeEnum;
import com.aftership.tracking.exception.ShipmentTrackingException;
import com.aftership.tracking.model.Checkpoint;
import com.aftership.tracking.model.NewTrackingRequest;
import com.aftership.tracking.model.ShipmentModel;
import com.aftership.tracking.model.Tracking;
import com.aftership.tracking.repository.ShipmentDetailsRepository;
import com.aftership.tracking.restutil.RestApiClient;
import com.aftership.tracking.service.ShipmentService;
import com.aftership.tracking.transformation.ShipmentEntityTransformation;
import com.aftership.tracking.transformation.ShipmentModelTransformation;
import com.aftership.tracking.transformation.TrackingTransformation;

@Service
public class ShipmentServiceImpl implements ShipmentService {

    private static final Logger logger = LoggerFactory.getLogger(ShipmentServiceImpl.class);

    @Autowired
    private RestApiClient restApiClient;

    @Autowired
    private ShipmentDetailsRepository shipmentDetailsRepository;

    @Autowired
    private TrackingTransformation trackingTransformation;

    @Autowired
    private ShipmentEntityTransformation shipmentEntityTransformation;

    @Autowired
    private ShipmentModelTransformation shipmentModelTransformation;

    /**
     * <p>
     * 1. Method validates shipment model object
     * 2. Checks the record already exists in h2db - tracking number and courier code
     *  - if exists -> throw record already found exception
     * 3. call aftership post api to create shipment tracking
     * 4. save shipment details in shipment_details table in h2 db
     * 5. return requestModel
     *</p>
     *
     * @param requestModel
     * @return requestModel
     * @throws ShipmentTrackingException
     */
    @Override
    public ShipmentModel createTracking(ShipmentModel requestModel) {
        logger.debug("In ShipmentServiceImpl :: called createTracking method for number : {} and courier code : {} ", requestModel.getTrackingNumber(), requestModel.getCourierCode());
        validateShipmentModel(requestModel);
        restApiClient.makePostRestCall(new NewTrackingRequest(trackingTransformation.apply(requestModel)));
        logger.debug("Successfully created shipment in aftership, for tracking number : {} and courier code : {} ", requestModel.getTrackingNumber(), requestModel.getCourierCode());
        ShipmentDetails entity = shipmentDetailsRepository.save(shipmentEntityTransformation.apply(requestModel));
        requestModel.setId(entity.getId());
        logger.debug("Successfully created for tracking number : {} and courier code : {} ", requestModel.getTrackingNumber(), requestModel.getCourierCode());
        return requestModel;
    }

    /**
     * Get shipment details by trackingNumber and courierCode
     * <p>
     * 1. Check shipment details exists in h2db, if not exists -> throw shipment details not found exception
     * 2. call aftership get api and set status to shipments
     * 3. return shipment details
     *
     * @param trackingNumber
     * @param courierCode
     * @return
     */
    @Override
    public ShipmentModel getTracking(String trackingNumber, String courierCode) {
        logger.debug("In ShipmentServiceImpl :: called getTracking method for number : {} and courier code : {} ", trackingNumber, courierCode);
        validateTrackingNumberAndCourierCode(trackingNumber, courierCode);
        ShipmentDetails shipmentDetails = shipmentDetailsRepository.findByTrackingNumberAndCourierCode(trackingNumber, courierCode);
        if (shipmentDetails == null) {
            logger.error("Tracking details not found for number : {} and courier code : {} ", trackingNumber, courierCode);
            throw new ShipmentTrackingException("Tracking number not found.");
        }
        Tracking tracking = restApiClient.makeGetRestCall(trackingNumber, courierCode);
        logger.debug("Found tracking details for number : {} and courier code : {} and sending for object transformation.", trackingNumber, courierCode);
        ShipmentModel shipmentModel = shipmentModelTransformation.apply(shipmentDetails);
        shipmentModel.setCurrentStatus(tracking.getTag());
        setOriginAndDestination(tracking, shipmentModel);
        logger.debug("Returning shipment details for tracking number : {} and courier code : {}", trackingNumber, courierCode);
        return shipmentModel;
    }

    private void setOriginAndDestination(Tracking tracking, ShipmentModel shipmentModel) {
        logger.debug("Set origin and destination to shipment details : {} and courier code : {} ", shipmentModel.getTrackingNumber(), shipmentModel.getCourierCode());
        List<Checkpoint> checkpoints = tracking.getCheckpoints();
        if("Delivered".equalsIgnoreCase(tracking.getTag()) && !CollectionUtils.isEmpty(checkpoints)) {
            shipmentModel.setOrigin(checkpoints.get(0).getLocation());
            shipmentModel.setDestination(checkpoints.get(checkpoints.size()-1).getLocation());
        }
    }

    private void validateTrackingNumberAndCourierCode(String trackingNumber, String courierCode) {
        logger.debug("Validating tracking number : {} and courier code : {} ", trackingNumber, courierCode);
        if (StringUtils.isEmpty(trackingNumber)) {
            throw new ShipmentTrackingException("Please provide valid tracking number");
        } else if (!CourierCodeEnum.getValues().contains(courierCode)) {
            throw new ShipmentTrackingException("Please provide valid courier code");
        }
        logger.debug("Successfully validated tracking number : {} and courier code : {} ", trackingNumber, courierCode);
    }

    private void validateShipmentModel(ShipmentModel requestModel) {
        String trackingNumber = requestModel.getTrackingNumber();
        String courierCode = requestModel.getCourierCode();
        logger.debug("Validating shipment details for tracking number : {} and courier code : {} ", trackingNumber, courierCode);
        validateTrackingNumberAndCourierCode(trackingNumber, courierCode);
        if (StringUtils.isEmpty(requestModel.getOrigin())) {
            throw new ShipmentTrackingException("Please provide valid origin information");
        } else if (StringUtils.isEmpty(requestModel.getDestination())) {
            throw new ShipmentTrackingException("Please provide valid destination information");
        }
        boolean isShipmentAlreadyExists = shipmentDetailsRepository.existsByTrackingNumberAndCourierCode(trackingNumber, courierCode);
        if (isShipmentAlreadyExists) {
            logger.error("Failed to created for tracking number : {} and courier code : {} as the record already exists", trackingNumber, courierCode);
            throw new ShipmentTrackingException("Tracking number already exists.");
        }
        logger.debug("Successfully validated shipment details for tracking number : {} and courier code : {} ", trackingNumber, courierCode);
    }

}
