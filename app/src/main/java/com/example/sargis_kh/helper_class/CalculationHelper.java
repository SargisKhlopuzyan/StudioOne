package com.example.sargis_kh.helper_class;

import android.app.Activity;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;

import com.example.sargis_kh.model.Company;
import com.example.sargis_kh.model.Vector2;

/**
 * Created by Sargis_Kh on 5/4/2016.
 */
public class CalculationHelper {

    private static final String CALCULATION_LOG = "CALCULATION_LOG";
    private static final double MAX_DISTANCE = 1000; // meter

    // camera field of view // in angle
    private static final int cameraFOV = 60;

    // Returns logo position on divace screen
    public static Vector2 getLogoPosition(Vector2 screenSize, Company company, Vector2 currentCoordinate, double azimuth) {
        Vector2 companyPosition = new Vector2(company.getLongitude(),company.getLatitude());
        if (getDistanceOfTwoCoordinates(currentCoordinate, companyPosition) > MAX_DISTANCE) {
//            Log.d(CALCULATION_LOG, "max distance : " + getDistanceOfTwoCoordinates(currentCoordinate, companyPosition));
            return null;
        }
        double angle = getAngle(company, azimuth, currentCoordinate);
        if (angle > -cameraFOV / 2 && angle < cameraFOV / 2) {
            angle = angle * Math.PI / 180;
            double x = screenSize.x()*(1 - ( Math.tan(angle) / Math.tan(cameraFOV * Math.PI / 360)) ) / 2;
            Vector2 logoPosition = new Vector2(x, screenSize.y()/2);
//            Log.d(CALCULATION_LOG, "angle :" + Math.tan(angle) / Math.tan(cameraFOV * Math.PI / 360));
            return logoPosition;
        } else {
            return null;
        }
    }

    // Gets approximate distance of two coordinate
    private static double getDistanceOfTwoCoordinates(Vector2 coordinateA, Vector2 coordinateB) {
        double R = 6371000; // metres
        double lat1 = coordinateA.y() * Math.PI / 180;
        double lat2 = coordinateB.y() * Math.PI / 180;
        double dLatitude = (lat2-lat1);
        double dLongitude = (coordinateB.x() - coordinateA.x()) * Math.PI / 180;

        double a = Math.sin(dLatitude/2) * Math.sin(dLatitude/2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.sin(dLongitude/2) * Math.sin(dLongitude/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double d = R * c;
        return Math.abs(d);
    }

    // Returns the angle between camera look direction and camera-company direction
    private static double getAngle(Company company, double azimuth, Vector2 currentCoordinate) {
        double angle = calculateTheNorthAngleBetweenPoints(currentCoordinate.x(), currentCoordinate.y(), company.getLongitude(), company.getLatitude());
        double diffAngle = azimuth - angle;
//        Log.d(CALCULATION_LOG, "angle :" + diffAngle);
        return diffAngle;
    }

    // Returns the North angle of line
    private static double calculateTheNorthAngleBetweenPoints(double x1,double y1,double x2,double y2) {
        double dx, dy, result;

        dx = x2 - x1;
        dy = y2 - y1;

        if (dx > 0) {
            result = (Math.PI*0.5) - Math.atan(dy / dx);
        } else if (dx < 0) {
            result = (Math.PI*1.5) - Math.atan(dy/dx);
        } else if (dy > 0) {
            result = 0;
        } else if (dy < 0) {
            result = Math.PI;
        } else {
            // the 2 points are equal
            result = -9999;
        }

        result = result * 180 / Math.PI;
        return result;
    }
}