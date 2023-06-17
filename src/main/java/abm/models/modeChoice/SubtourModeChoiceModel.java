package abm.models.modeChoice;

import abm.data.DataSet;
import abm.data.plans.Mode;
import abm.data.plans.Purpose;
import abm.data.plans.Tour;
import abm.data.pop.EmploymentStatus;
import abm.io.input.CoefficientsReader;
import abm.properties.AbitResources;
import de.tum.bgu.msm.data.person.Occupation;
import de.tum.bgu.msm.util.MitoUtil;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class SubtourModeChoiceModel implements SubtourModeChoice {

    private final DataSet dataSet;
    private Map<String, Double> switchToWalkCoef;

    public SubtourModeChoiceModel(DataSet dataSet) {

        this.dataSet = dataSet;
        this.switchToWalkCoef =
                new CoefficientsReader(dataSet, "switchToWalk",
                        Path.of(AbitResources.instance.getString("mode.choice.subtour"))).readCoefficients();

    }

    @Override
    public void chooseSubtourMode(Tour tour) {

        double utility = calculateUtility(tour);

        Map<Mode, Double> subtourModes = new HashMap<>();
        subtourModes.put(tour.getTourMode(), Math.exp(0.0));
        subtourModes.put(Mode.WALK, Math.exp(utility));


        Mode subtourMode = MitoUtil.select(subtourModes);


        tour.getMainActivity().getSubtour().getInboundLeg().setLegMode(subtourMode);
        tour.getMainActivity().getSubtour().getOutboundLeg().setLegMode(subtourMode);

    }

    private double calculateUtility(Tour tour) {
        double utility = 0.;
        double distance = tour.getMainActivity().getSubtour().getOutboundLeg().getDistance();


        utility += switchToWalkCoef.get("log(distanceKm+1)") * Math.log(distance + 1);

        if (tour.getMainActivity().getPerson().getEmploymentStatus().equals(EmploymentStatus.FULLTIME_EMPLOYED)){
            utility += switchToWalkCoef.get("isOccupation_Employed");
        } else if(tour.getMainActivity().getPerson().getEmploymentStatus().equals(EmploymentStatus.HALFTIME_EMPLOYED)){
            utility += switchToWalkCoef.get("isOccupation_Halftime");
        }

        if (tour.getTourMode().equals(Mode.CAR_DRIVER)){
            utility += switchToWalkCoef.get("isTourMainMode_CarD");
        }

        if (tour.getMainActivity().getPurpose().equals(Purpose.EDUCATION)){
            utility += switchToWalkCoef.get("isTourPurpose_Education");
        }

        return utility;
    }


}
