package com.kicc.easypos.tablet.filedownload;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DoubleConverter;
import com.thoughtworks.xstream.converters.basic.FloatConverter;
import com.thoughtworks.xstream.converters.basic.IntConverter;
import com.thoughtworks.xstream.converters.basic.LongConverter;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.Xpp3Driver;
import com.thoughtworks.xstream.mapper.MapperWrapper;

/**
 * Created by moonkikim on 2015-09-01.
 */
public class ConvertUtil {

    /**
     * XML 스트링을 Xml Object 전환
     *
     * @param response
     * @param classes
     * @return
     */
    public static Object convertXmlToObject(String response, Class... classes) {
        Object retObject = null;

//        XmlFriendlyReplacer replacer = new XmlFriendlyReplacer("__", "_");
        XmlFriendlyNameCoder replacer = new XmlFriendlyNameCoder("__", "_");

//        XStream xs = new XStream(new DomDriver("UTF-8", replacer)) {
//            @Override
//            protected MapperWrapper wrapMapper(MapperWrapper next) {
//                return new MapperWrapper(next) {
//                    @Override
//                    public boolean shouldSerializeMember(Class definedIn, String fieldName) {
//                        if (definedIn == Object.class) {
//                            try {
//                                return this.realClass(fieldName) != null;
//                            } catch (Exception e) {
////                                e.printStackTrace();
//                                return false;
//                            }
//                        } else {
//                            return super.shouldSerializeMember(definedIn, fieldName);
//                        }
//                    }
//
//                };
//            }
//
//            protected boolean useXStream11XmlFriendlyMapper() {
//                return true;
//            }
//        };


        XStream xs = new XStream(new Xpp3Driver(replacer)) {
            @Override
            protected MapperWrapper wrapMapper(MapperWrapper next) {
                return new MapperWrapper(next) {
                    @Override
                    public boolean shouldSerializeMember(Class definedIn, String fieldName) {
                        if (definedIn == Object.class) {
                            try {
                                return this.realClass(fieldName) != null;
                            } catch (Exception e) {
//                                e.printStackTrace();
                                return false;
                            }
                        } else {
                            return super.shouldSerializeMember(definedIn, fieldName);
                        }
                    }

                };
            }

//            protected boolean useXStream11XmlFriendlyMapper() {
//                return true;
//            }
        };
        XStream.setupDefaultSecurity(xs);

        xs.registerConverter(new IntConverter() {
            @Override
            public Object fromString(String str) {
                if (str.compareTo("") == 0) {
                    str = "0";
                } else {
                    if (str.indexOf(".") > 0) {
                        str = str.substring(0, str.indexOf("."));
                    }
                }
                return super.fromString(str);
            }
        });
        xs.registerConverter(new LongConverter() {
            @Override
            public Object fromString(String str) {
                if (str.compareTo("") == 0) {
                    str = "0";
                } else {
                    if (str.indexOf(".") > 0) {
                        str = str.substring(0, str.indexOf("."));
                    }
                }

                return super.fromString(str);
            }
        });
        xs.registerConverter(new FloatConverter() {
            @Override
            public Object fromString(String str) {
                if (str.compareTo("") == 0) {
                    str = "0.0";
                }
                return super.fromString(str);
            }
        });
        xs.registerConverter(new DoubleConverter() {
            @Override
            public Object fromString(String str) {
                if (str.compareTo("") == 0) {
                    str = "0.0";
                }
                return super.fromString(str);
            }
        });

        for (Class cls : classes) {
            xs.processAnnotations(cls);
        }
        xs.allowTypes(classes);
        try {
            retObject = xs.fromXML(response);
        } catch (Exception e) {
            return null;
        }
        return retObject;
    }

    /**
     * XML Object 에서 Xml 스트링으로 변경
     *
     * @param object
     * @param classes
     * @return
     */
    public static String convertObjectToXml(Object object, Class... classes) {

        String retXml = null;

//        XmlFriendlyReplacer replacer = new XmlFriendlyReplacer("__", "_");
        XmlFriendlyNameCoder replacer = new XmlFriendlyNameCoder("__", "_");

        XStream xs;

//        if (classes[classes.length - 1].getSimpleName().equals("SaleInfo")) {
//
//            SortableFieldKeySorter sorter = new SortableFieldKeySorter();
//            sorter.registerFieldOrder(SaleInfo.class, new String[]{
//                    "saleHeader",
//                    "saleDetailList",
//                    "saleInfoCashSlipList",
//                    "saleInfoCardSlipList",
//                    "saleInfoCorpSlipList",
//                    "saleInfoCouponSlipList",
//                    "saleInfoGiftSlipList",
//                    "saleInfoTickSlipList",
//                    "saleInfoCoSlipList",
//                    "response"
//            });
//            xs = new XStream(new Sun14ReflectionProvider(new FieldDictionary(sorter)),
//                    new DomDriver("UTF-8", replacer)) {
//                protected boolean useXStream11XmlFriendlyMapper() {
//                    return true;
//                }
//            };
//
//        } else {
//        xs = new XStream(new DomDriver("UTF-8", replacer)) {
//            protected boolean useXStream11XmlFriendlyMapper() {
//                return true;
//            }
//        };
//        xs = new XStream(new Xpp3Driver(replacer)) {
//            protected boolean useXStream11XmlFriendlyMapper() {
//                return true;
//            }
//        };

        xs = new XStream(new Xpp3Driver(replacer));
//        xs = new XStream(new StaxDriver(replacer));
//        xs = new XStream(new WstxDriver(replacer));

//        }

        XStream.setupDefaultSecurity(xs);
        for (Class cls : classes) {
            xs.processAnnotations(cls);
        }
        xs.allowTypes(classes);


        retXml = xs.toXML(object);

        return retXml;
    }



}
