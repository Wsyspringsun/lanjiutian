package com.wyw.ljtds.biz.biz;

import java.io.IOException;
import java.net.SocketTimeoutException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.NetWorkNotAvailableException;
import com.wyw.ljtds.biz.exception.OperationTimeOutException;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.MyApplication;
import com.wyw.ljtds.config.PreferenceCache;
import com.wyw.ljtds.utils.Base64;
import com.wyw.ljtds.utils.NetworkDetector;
import com.wyw.ljtds.utils.Utils;


public class SoapProcessor {

    public static enum PropertyType {
        TYPE_STRING(0), TYPE_INTEGER(1), TYPE_LONG(2), TYPE_BOOLEAN(3), TYPE_OBJECT(
                4);

        public int value;

        PropertyType(int aValue) {
            value = aValue;
        }
    }

    private String mUrl;

    private String mMethodName;

    private SoapObject mRequest;

    public SoapProcessor() {

    }

    public SoapProcessor(String wsClass, String method) {
        this(wsClass, method, true);
    }

    public SoapProcessor(String wsClass, String method, boolean needToken) {
        this(AppConfig.WS_NAME_SPACE, AppConfig.WS_BASE_URL + wsClass, method,
                needToken);
    }

    public SoapProcessor(String nameSpace, String url, String method,
                         boolean needToken) {
        mUrl = url;
        mMethodName = method;
        mRequest = new SoapObject(nameSpace, method);

        if (needToken) {
            setProperty("token", PreferenceCache.getToken(),
                    PropertyType.TYPE_STRING);
        }
    }


    public String requestStr() throws BizFailure, ZYException {

        if (!NetworkDetector.isNetworkAvailable(MyApplication.getAppContext())) {
            throw new NetWorkNotAvailableException();
        }

        HttpTransportSE androidHttpTransport = new HttpTransportSE(mUrl, 10000);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);

        envelope.bodyOut = mRequest;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(mRequest);

        new MarshalBase64().register(envelope); // 传递byte[]时需要序列化

        SoapObject response = null;

		/*
         * List<HeaderProperty> hps = new ArrayList<HeaderProperty>();
		 * hps.add(new HeaderProperty("Cookie", PreferenceCache.getSession()));
		 * androidHttpTransport.call(BccfConfig.WS_NAME_SPACE + "/" +
		 * mMethodName, envelope, hps);
		 */
        try {
            androidHttpTransport.call(null, envelope);
            if (envelope.bodyIn instanceof SoapFault) {
                Log.e(AppConfig.ERR_TAG, envelope.bodyIn.toString());
            }

            response = (SoapObject) (envelope.bodyIn);
        } catch (IOException e) {
            if (e instanceof SocketTimeoutException) {
                throw new OperationTimeOutException(e);
            }
            throw new ZYException(e);
        } catch (XmlPullParserException e) {
            throw new ZYException(e);
        } catch (ClassCastException e) {
            throw new ZYException(e);
        }

        String result = response.getProperty("return").toString();

        // LogUtil.e(result);
        Log.e(AppConfig.ERR_TAG, result);

        return result;
    }


    public JsonElement request() throws BizFailure, ZYException {
        if (!NetworkDetector.isNetworkAvailable(MyApplication.getAppContext())) {
            throw new NetWorkNotAvailableException();
        }
        HttpTransportSE androidHttpTransport = new HttpTransportSE(mUrl, 10000);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);

        envelope.bodyOut = mRequest;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(mRequest);

        new MarshalBase64().register(envelope); // 传递byte[]时需要序列化

        SoapObject response = null;

		/*
         * List<HeaderProperty> hps = new ArrayList<HeaderProperty>();
		 * hps.add(new HeaderProperty("Cookie", PreferenceCache.getSession()));
		 * androidHttpTransport.call(BccfConfig.WS_NAME_SPACE + "/" +
		 * mMethodName, envelope, hps);
		 */
        try {
            androidHttpTransport.call(null, envelope);
            if (envelope.bodyIn instanceof SoapFault) {
                Log.e(AppConfig.ERR_TAG, envelope.bodyIn.toString());
            }

            response = (SoapObject) (envelope.bodyIn);
        } catch (IOException e) {
            if (e instanceof SocketTimeoutException) {
                throw new OperationTimeOutException(e);
            }
            throw new ZYException(e);
        } catch (XmlPullParserException e) {
            throw new ZYException(e);
        } catch (ClassCastException e) {
            throw new ZYException(e);
        }

        String result = response.getProperty("return").toString();

        // LogUtil.e(result);
        Utils.log("soap response:" + result);

        return parseResponse(result);
    }

    private static final String RESPONSE_STATUS = "status";
    private static final String RESPONSE_MESSAGE = "message";
    private static final String RESPONSE_RESULT = "result";

    private JsonElement parseResponse(String response) throws ZYException,
            BizFailure {
        try {
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(response).getAsJsonObject();
            JsonElement resultElement;

            boolean status = jsonObject.get(RESPONSE_STATUS).getAsBoolean();
            if (!status) {
                String message = jsonObject.get(RESPONSE_MESSAGE).getAsString();
                throw new BizFailure(message);
            }

            resultElement = jsonObject.get(RESPONSE_RESULT);

            return resultElement;
        } catch (Exception e) {
            if (e instanceof BizFailure) {
                throw (BizFailure) e;
            } else {
                throw new ZYException(e);
            }
        }
    }

    public byte[] requestBytes() throws IOException, XmlPullParserException {

        HttpTransportSE androidHttpTransport = new HttpTransportSE(mUrl, 90000);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);

        envelope.bodyOut = mRequest;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(mRequest);

        SoapObject response = null;

        androidHttpTransport.call(null, envelope);
        if (envelope.bodyIn instanceof SoapFault) {
            // LogUtil.e(envelope.bodyIn.toString());
        }

        response = (SoapObject) (envelope.bodyIn);

        return Base64.decode(response.getProperty(mMethodName + "Return")
                .toString());
    }

    public void setProperty(String propertyName, Object propertyValue,
                            PropertyType type) {
        // LogUtil.e(propertyName + ":" + propertyValue.toString());

        PropertyInfo pi = new PropertyInfo();
        Object typeValue = null;
        switch (type) {
            case TYPE_STRING:
                typeValue = PropertyInfo.STRING_CLASS;
                break;
            case TYPE_INTEGER:
                typeValue = PropertyInfo.INTEGER_CLASS;
                break;
            case TYPE_LONG:
                typeValue = PropertyInfo.LONG_CLASS;
                break;
            case TYPE_BOOLEAN:
                typeValue = PropertyInfo.BOOLEAN_CLASS;

                break;
            case TYPE_OBJECT:
                typeValue = MarshalBase64.BYTE_ARRAY_CLASS;

                break;
            default:
                typeValue = PropertyInfo.STRING_CLASS;
                break;
        }

        pi.setType(typeValue);
        pi.setName(propertyName);
        pi.setValue(propertyValue);

        mRequest.addProperty(pi);
    }

}
