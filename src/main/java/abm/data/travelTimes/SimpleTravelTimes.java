package abm.data.travelTimes;

import abm.data.geo.Location;
import abm.data.geo.MicroscopicLocation;
import abm.data.plans.Mode;

public class SimpleTravelTimes implements TravelTimes{

    @Override
    public double getTravelTimeInSeconds(Location origin, Location destination, Mode mode, double time){
        double distance_m = Math.abs(((MicroscopicLocation) origin).getX() - ((MicroscopicLocation)destination).getX()) +
                Math.abs(((MicroscopicLocation) origin).getY() - ((MicroscopicLocation)destination).getY());

        double speed_ms = 15. / 3.6;

        return distance_m / speed_ms;
    }
}
