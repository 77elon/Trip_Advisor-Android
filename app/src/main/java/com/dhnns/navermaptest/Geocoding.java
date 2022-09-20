package com.dhnns.navermaptest;

public class Geocoding
{
    private String[] location;

    public String[] getLocation ()
    {

        return location;
    }

    public void setLocation (String[] location)
    {
        this.location = location;
    }

    @Override
    public String toString()
    {
        return "[location = "+location+"]";
    }


    public static class Response
    {
        private static String code;

        private Route route;

        private String currentDateTime;

        private String message;

        public static String getCode()
        {
            return code;
        }

        public void setCode (String code)
        {
            this.code = code;
        }

        public Route getRoute ()
        {
            return route;
        }

        public void setRoute (Route route)
        {
            this.route = route;
        }

        public String getCurrentDateTime ()
        {
            return currentDateTime;
        }

        public void setCurrentDateTime (String currentDateTime)
        {
            this.currentDateTime = currentDateTime;
        }

        public String getMessage ()
        {
            return message;
        }

        public void setMessage (String message)
        {
            this.message = message;
        }

        @Override
        public String toString()
        {
            return "[code = "+code+", route = "+route+", currentDateTime = "+currentDateTime+", message = "+message+"]";
        }
    }


    public class Route
    {
        private Trafast[] trafast;

        public Trafast[] getTrafast ()
        {
            return trafast;
        }

        public void setTrafast (Trafast[] trafast)
        {
            this.trafast = trafast;
        }

        @Override
        public String toString()
        {
            return "[trafast = "+trafast+"]";
        }
    }


    public class Trafast
    {
        private Summary summary;

        private String[][] path;

        private Section[] section;

        private Guide[] guide;

        public Summary getSummary ()
        {
            return summary;
        }

        public void setSummary (Summary summary)
        {
            this.summary = summary;
        }

        public String[][] getPath ()
        {
            return path;
        }

        public void setPath (String[][] path)
        {
            this.path = path;
        }

        public Section[] getSection ()
        {
            return section;
        }

        public void setSection (Section[] section)
        {
            this.section = section;
        }

        public Guide[] getGuide ()
        {
            return guide;
        }

        public void setGuide (Guide[] guide)
        {
            this.guide = guide;
        }

        @Override
        public String toString()
        {
            return "[summary = "+summary+", path = "+path+", section = "+section+", guide = "+guide+"]";
        }
    }

    public class Guide
    {
        private String duration;

        private String instructions;

        private String pointIndex;

        private String distance;

        private String type;

        public String getDuration ()
        {
            return duration;
        }

        public void setDuration (String duration)
        {
            this.duration = duration;
        }

        public String getInstructions ()
        {
            return instructions;
        }

        public void setInstructions (String instructions)
        {
            this.instructions = instructions;
        }

        public String getPointIndex ()
        {
            return pointIndex;
        }

        public void setPointIndex (String pointIndex)
        {
            this.pointIndex = pointIndex;
        }

        public String getDistance ()
        {
            return distance;
        }

        public void setDistance (String distance)
        {
            this.distance = distance;
        }

        public String getType ()
        {
            return type;
        }

        public void setType (String type)
        {
            this.type = type;
        }

        @Override
        public String toString()
        {
            return "[duration = "+duration+", instructions = "+instructions+", pointIndex = "+pointIndex+", distance = "+distance+", type = "+type+"]";
        }
    }

    public class Section
    {
        private String pointIndex;

        private String pointCount;

        private String distance;

        private String name;

        private String congestion;

        private String speed;

        public String getPointIndex ()
        {
            return pointIndex;
        }

        public void setPointIndex (String pointIndex)
        {
            this.pointIndex = pointIndex;
        }

        public String getPointCount ()
        {
            return pointCount;
        }

        public void setPointCount (String pointCount)
        {
            this.pointCount = pointCount;
        }

        public String getDistance ()
        {
            return distance;
        }

        public void setDistance (String distance)
        {
            this.distance = distance;
        }

        public String getName ()
        {
            return name;
        }

        public void setName (String name)
        {
            this.name = name;
        }

        public String getCongestion ()
        {
            return congestion;
        }

        public void setCongestion (String congestion)
        {
            this.congestion = congestion;
        }

        public String getSpeed ()
        {
            return speed;
        }

        public void setSpeed (String speed)
        {
            this.speed = speed;
        }

        @Override
        public String toString()
        {
            return "[pointIndex = "+pointIndex+", pointCount = "+pointCount+", distance = "+distance+", name = "+name+", congestion = "+congestion+", speed = "+speed+"]";
        }
    }

    public class Summary
    {
        private String duration;

        private Goal goal;

        private String tollFare;

        private String distance;

        private String[][] bbox;

        private Geocoding geocoding;

        private String fuelPrice;

        private String taxiFare;

        public String getDuration ()
        {
            return duration;
        }

        public void setDuration (String duration)
        {
            this.duration = duration;
        }

        public Goal getGoal ()
        {
            return goal;
        }

        public void setGoal (Goal goal)
        {
            this.goal = goal;
        }

        public String getTollFare ()
        {
            return tollFare;
        }

        public void setTollFare (String tollFare)
        {
            this.tollFare = tollFare;
        }

        public String getDistance ()
        {
            return distance;
        }

        public void setDistance (String distance)
        {
            this.distance = distance;
        }

        public String[][] getBbox ()
        {
            return bbox;
        }

        public void setBbox (String[][] bbox)
        {
            this.bbox = bbox;
        }

        public Geocoding getGeo ()
        {
            return geocoding;
        }

        public void setStart (Geocoding geocoding)
        {
            this.geocoding = geocoding;
        }

        public String getFuelPrice ()
        {
            return fuelPrice;
        }

        public void setFuelPrice (String fuelPrice)
        {
            this.fuelPrice = fuelPrice;
        }

        public String getTaxiFare ()
        {
            return taxiFare;
        }

        public void setTaxiFare (String taxiFare)
        {
            this.taxiFare = taxiFare;
        }

        @Override
        public String toString()
        {
            return "[duration = "+duration+", goal = "+goal+", tollFare = "+tollFare+", distance = "+distance+", bbox = "+bbox+", Geocoding = "+ geocoding +", fuelPrice = "+fuelPrice+", taxiFare = "+taxiFare+"]";
        }
    }

    public class Goal
    {
        private String[] location;

        private String dir;

        public String[] getLocation ()
        {
            return location;
        }

        public void setLocation (String[] location)
        {
            this.location = location;
        }

        public String getDir ()
        {
            return dir;
        }

        public void setDir (String dir)
        {
            this.dir = dir;
        }

        @Override
        public String toString()
        {
            return "[location = "+location+", dir = "+dir+"]";
        }
    }


}

