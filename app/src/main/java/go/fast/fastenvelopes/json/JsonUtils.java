/*
    Android Asynchronous Http Client
    Copyright (c) 2011 James Smith <james@loopj.com>
    https://loopj.com

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        https://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */

package go.fast.fastenvelopes.json;

import android.content.Context;
import android.util.Log;

import com.google.myjson.Gson;
import com.google.myjson.GsonBuilder;
import com.google.myjson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import go.fast.fastenvelopes.utils.ToolUtils;


/**
 * Class meant to be used with custom JSON parser (such as GSON or Jackson JSON)
 * <p>
 * &nbsp;
 * </p>
 * {@link #parseResponse(String, boolean)} should be overriden and must return
 * type of generic param class, response will be then handled to implementation
 * of abstract methods
 * {@link #onSuccess(int, cz.msebera.android.httpclient.Header[], String, Object)}
 * or
 * {@link #onFailure(int, cz.msebera.android.httpclient.Header[], Throwable, String, Object)}
 * , depending of response HTTP status line (result http code)
 *
 * @param <JSON_TYPE>
 *            Generic type meant to be returned in callback
 */
public class JsonUtils<JSON_TYPE> {

    public final static Gson gGson;
    private static Class dataType = null;

    static {
	gGson = new GsonBuilder().setExclusionStrategies(
		new NoSerializeExclusionStrategy()).create();
    }

    private Gson mGson;

    public JsonUtils setGson(Gson gson) {
	this.mGson = gson;

	return this;
    }

    public JsonUtils(Class dataType) {
	this.dataType = dataType;
    }

    public JSON_TYPE parseResponseNow(String rawJsonData, boolean isFailure)
	    throws Throwable {
	Object obj = null;
	try {
	    if (rawJsonData == null || "".equals(rawJsonData)
		    || rawJsonData.length() == 0) {
	    }

	    if (dataType != null) {
		if (mGson == null) {
		    obj = gGson.fromJson(rawJsonData, dataType);
		} else {
		    obj = mGson.fromJson(rawJsonData, dataType);
		}
	    } else {
		obj = new JSONObject(rawJsonData);
	    }

	} catch (JsonSyntaxException e) {
	    System.out.println("   JsonSyntaxException   Throwable  "+e);
	    Log.e("JsonSyntaxException", e.toString());

	} catch (JSONException e) {
	    System.out.println("   JSONException   Throwable  "+e);
	}
	return (JSON_TYPE) obj;
    }
    
    
    public  JSON_TYPE ParseSomething(Context context ,String rawJsonData)
    {
	Object obj = null;
	try {
	    
	    System.out.println("  解析的内容字符串：  "+rawJsonData);
	    obj=  parseResponseNow( rawJsonData, false);
	} catch (Throwable e) {
	    System.out.println("   ParseSomething   Throwable  "+e);
	    ToolUtils.showToast(context, "解析异常");
	    e.printStackTrace();
	}
	return (JSON_TYPE) obj;
    }

    public Class getDataType() {
	return dataType;
    }

    public void setDataType(Class dataType) {
	this.dataType = dataType;
    }

}
