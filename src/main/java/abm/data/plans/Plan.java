package abm.data.plans;

import abm.data.pop.Person;
import abm.data.timeOfDay.AvailableTimeOfWeek;
import abm.data.timeOfDay.AvailableTimeOfWeekLinkedList;
import abm.utils.PlanTools;

import java.util.*;

public class Plan {

    private Person person;
    private int id;

    //private SortedMap<Integer, Activity> homeActivities;

    private Activity dummyHomeActivity;
    private SortedMap<Integer, Tour> tours;
    private AvailableTimeOfWeekLinkedList availableTimeOfWeek;
    private Plan() {

    }

    public SortedMap<Integer, Tour> getTours() {
        return tours;
    }

    public static Plan initializePlan(Person person) {
        Plan plan = new Plan();
        plan.availableTimeOfWeek = new AvailableTimeOfWeekLinkedList();
        plan.id = person.getId();
        plan.person = person;
        //plan.homeActivities = new TreeMap<>();
        final Activity homeActivity = new Activity(person, Purpose.HOME);
        homeActivity.setStartTime_min(PlanTools.startOfTheWeek());
        homeActivity.setEndTime_min(PlanTools.endOfTheWeek());
        homeActivity.setLocation(person.getHousehold().getLocation());
        //plan.homeActivities.put(ScheduleUtils.startOfTheWeek(), homeActivity);
        plan.dummyHomeActivity = new Activity(person, Purpose.HOME);
        plan.dummyHomeActivity.setStartTime_min(PlanTools.startOfTheWeek());
        plan.dummyHomeActivity.setEndTime_min(PlanTools.endOfTheWeek());
        plan.dummyHomeActivity.setLocation(person.getHousehold().getLocation());
        plan.tours = new TreeMap<>();
        person.setPlan(plan);
        return plan;
    }


    public Person getPerson() {
        return person;
    }


    /*public SortedMap<Integer, Activity> getHomeActivities() {
        return homeActivities;
    }*/

    public String logPlan(double interval_s) {
        double time = PlanTools.startOfTheWeek() + interval_s / 2;
        StringBuilder string = new StringBuilder();
        string.append(person.getId()).append(",").append(person.getHousehold().getId()).append(",");
        int size = 0;
        while (time <= PlanTools.endOfTheWeek()) {
           /* for (Activity a : homeActivities.values()) {
                if (time <= a.getEndTime_min() && time > a.getStartTime_min()) {
                    string.append(a.getPurpose().toString()).append(",");
                    size++;
                    time += interval_s;
                    break;
                }
            }*/
            for (Tour tour : tours.values()) {
                for (Activity a : tour.getActivities().values()) {
                    if (time <= a.getEndTime_min() && time > a.getStartTime_min()) {
                        string.append(a.getPurpose().toString()).append(",");
                        size++;
                        time += interval_s;
                        break;
                    }
                }
                for (Leg t : tour.getLegs().values()) {
                    if (time >= t.getPreviousActivity().getEndTime_min() && time < t.getNextActivity().getStartTime_min()) {
                        string.append("T" + ",");
                        size++;
                        time += interval_s;
                        break;
                    }
                }
                for (Tour subtour : tour.getSubtours().values()) {
                    for (Activity a : subtour.getActivities().values()) {
                        if (time <= a.getEndTime_min() && time > a.getStartTime_min()) {
                            string.append(a.getPurpose().toString()).append(",");
                            size++;
                            time += interval_s;
                            break;
                        }
                    }
                    for (Leg t : subtour.getLegs().values()) {
                        if (time >= t.getPreviousActivity().getEndTime_min() && time < t.getNextActivity().getStartTime_min()) {
                            string.append("T" + ",");
                            size++;
                            time += interval_s;
                            break;
                        }
                    }
                }

            }



        }
        string.append("H");
        return string.toString();
    }

    public AvailableTimeOfWeekLinkedList getAvailableTimeOfDay() {
        return availableTimeOfWeek;
    }


    public Activity getDummyHomeActivity() {
        return dummyHomeActivity;
    }
}
