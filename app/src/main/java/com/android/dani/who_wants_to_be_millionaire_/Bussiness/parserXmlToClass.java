package com.android.dani.who_wants_to_be_millionaire_.Bussiness;



import com.android.dani.who_wants_to_be_millionaire_.Data.Question;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/**
 * Created by dabasra on 24/05/2015.
 */
public class parserXmlToClass {

    private String text;

    private  InputStream file;
    private List<Question> list;
    public parserXmlToClass( InputStream is ) {
        file=is;
        list = new ArrayList <Question>();
        XmlPullParserFactory factory = null;
        XmlPullParser parser = null;
        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            parser = factory.newPullParser();
            parser.setInput(file, null);

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT ) {
                String tagname = parser.getName();
                if ( XmlPullParser.START_TAG ==eventType ) {
                    // create a new instance of employee
                    if (tagname.equalsIgnoreCase("question")) {
                        Question q=new Question();
                        //recorremos los atributos y los encapsulamos en la clase Question
                        for (int i = 0; i < parser.getAttributeCount(); i++) {
                            text = parser.getAttributeValue(i);
                            switch (i) {
                                case 0:
                                    //answer1="King John"
                                    q.setAnswer1(text);
                                    break;
                                case 1:
                                    //answer2="King Henry VIII"
                                    q.setAnswer2(text);
                                    break;
                                case 2:
                                    //answer3="King Richard the Lion-Hearted"
                                    q.setAnswer3(text);
                                    break;
                                case 3:
                                    //answer4="King George III"
                                    q.setAnswer4(text);
                                    break;
                                case 4:
                                    //audience="3"
                                    q.setAudience(text);
                                    break;
                                case 5:
                                    //fifty1="2"
                                    q.setFifty1(text);
                                    break;
                                case 6:
                                    //fifty2="3"
                                    q.setFifty2(text);
                                    break;
                                case 7:
                                    //number="4"
                                    q.setNumber(text);
                                    break;
                                case 8:
                                    //phone="1"
                                    q.setPhone(text);
                                    break;
                                case 9:
                                    //right="1"
                                    q.setRight(text);
                                    break;
                                case 10:
                                    //text="Who was forced to sign the Magna Carta?"
                                    q.setText(text);
                                    break;
                            }

                        }
                        list.add(q);
                    }
                }
                eventType = parser.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Question getQuestion(int qnumber){
        return list.get(qnumber);
    }

}



