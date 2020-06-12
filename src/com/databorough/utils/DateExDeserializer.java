package com.databorough.utils;

import java.io.IOException;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

/**
 * Class that defines API used by ObjectMapper (and other chained
 * JsonDeserializers too) to deserialize Object of DateEx type from JSON, using
 * provided JsonParser.
 *
 * @author Amit Arya
 */
public class DateExDeserializer extends JsonDeserializer<DateEx>
{
	@Override
	public DateEx deserialize(JsonParser jsonParser,
		DeserializationContext deserializationcontext) throws IOException {
		ObjectCodec oc = jsonParser.getCodec();
		JsonNode node = oc.readTree(jsonParser);

		String dateStr;
		char typeCode;

		if (node.isObject())
		{
			// {"typeCode":"D","date":"2014-10-07","time":"00:00:00",
			// "timestamp":1412654400000,"dateTime":1412654400000,"javaFormat":
			// "yyyy-MM-dd"}
			dateStr = node.get("date").getTextValue();
			typeCode = node.get("typeCode").getTextValue().charAt(0);
		}
		else
		{
			// "2014-10-07"
			dateStr = node.getTextValue();
			typeCode = 'D';
		}

		return DateEx.valueOf(dateStr, typeCode);
	}
}