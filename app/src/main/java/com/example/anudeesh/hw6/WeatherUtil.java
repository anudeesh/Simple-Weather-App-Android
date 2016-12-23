package com.example.anudeesh.hw6;

import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Anudeesh on 10/19/2016.
 */
public class WeatherUtil {
    static public class WeatherPullParser extends DefaultHandler {
        static public ArrayList<Weather> parseWeatherDetails(InputStream in) throws XmlPullParserException, IOException {
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(in,"UTF-8");
            ArrayList<Weather> weatherList = new ArrayList<Weather>();
            Weather wt = null;
            int event = parser.getEventType();

            while(event!=XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_TAG :
                        if(parser.getName().equals("time")) {
                            wt = new Weather();
                            wt.setTime(parser.getAttributeValue(null,"from"));
                        }
                        else if (parser.getName().equals("symbol")) {
                            wt.setIcon(parser.getAttributeValue(null,"var"));
                        }
                        else if (parser.getName().equals("temperature")) {
                            wt.setTemperature(parser.getAttributeValue(null,"value"));
                            //wt.setTemperature(parser.nextText().trim());
                        }
                        else if (parser.getName().equals("clouds")) {
                            wt.setCondition(parser.getAttributeValue(null,"value"));
                        }
                        else if (parser.getName().equals("pressure")) {
                            wt.setPressure(parser.getAttributeValue(null,"value")+" hPa");
                        }
                        else if (parser.getName().equals("humidity")) {
                            wt.setHumidity(parser.getAttributeValue(null,"value")+"%");
                        }
                        else if (parser.getName().equals("windSpeed")) {
                            wt.setWindSpeed(parser.getAttributeValue(null,"mps")+" mps");
                        }
                        else if (parser.getName().equals("windDirection")) {
                            wt.setWindDirection(parser.getAttributeValue(null,"deg")+"° "+parser.getAttributeValue(null,"code"));
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("time")) {
                            weatherList.add(wt);
                            wt=null;
                        }
                        break;
                    default:
                        break;
                }
                event = parser.next();
            }
            return weatherList;
        }
    }
}
