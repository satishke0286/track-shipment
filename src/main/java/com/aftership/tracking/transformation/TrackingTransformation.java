package com.aftership.tracking.transformation;

import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.aftership.tracking.model.NewTracking;
import com.aftership.tracking.model.ShipmentModel;

@Component
public class TrackingTransformation implements Function<ShipmentModel, NewTracking> {

    @Override
    public NewTracking apply(ShipmentModel model) {
        NewTracking newTracking = new NewTracking();
        newTracking.setSlug(new String[]{model.getCourierCode()});
        newTracking.setTrackingNumber(model.getTrackingNumber());
        newTracking.setDestinationCountryIso3(getCountryCode(model.getDestination()));
        newTracking.setOriginCountryIso3(getCountryCode(model.getOrigin()));
        return newTracking;
    }

    private static String getCountryCode(String address) {
        String[] addressSplit = address.split(",");
        return addressSplit[addressSplit.length - 1].replaceAll("\\d", "").trim();
    }

}
