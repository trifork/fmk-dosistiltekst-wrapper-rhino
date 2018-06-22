package dk.medicinkortet.fmkdosistiltekstwrapper.rhino;

import java.io.Reader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import org.mozilla.javascript.ConsString;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.Scriptable;

import com.fasterxml.jackson.core.JsonProcessingException;

import dk.medicinkortet.fmkdosistiltekstwrapper.DailyDosis;
import dk.medicinkortet.fmkdosistiltekstwrapper.DosageProposalResult;
import dk.medicinkortet.fmkdosistiltekstwrapper.DosageTranslation;
import dk.medicinkortet.fmkdosistiltekstwrapper.DosageTranslationCombined;
import dk.medicinkortet.fmkdosistiltekstwrapper.DosageType;
import dk.medicinkortet.fmkdosistiltekstwrapper.FMKVersion;
import dk.medicinkortet.fmkdosistiltekstwrapper.JSONHelper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.DosageWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.Interval;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.UnitOrUnitsWrapper;

public class DosisTilTekstWrapper {

	public class ConsoleObject {
		public void log(String level, String msg) {
			System.out.println(msg);
		}
	}
	
	private static ScriptEngine engine = null;
	private static CompiledScript script;
	private static Invocable invocable;
	private static Object combinedTextConverterObj;
	private static Object shortTextConverterObj;
	private static Object longTextConverterObj;
	private static Object dosageTypeCalculatorObj;
	private static Object dosageTypeCalculator144Obj;
	private static Object dailyDosisCalculatorObj;
	private static Object dosageProposalXMLGeneratorObj;
	
	public static void initialize(Reader javascriptFileReader) throws ScriptException {
		if(engine == null) {
			
			ScriptEngineManager factory = new ScriptEngineManager(); 
			engine = factory.getEngineByName("rhino17R4");
			
			Compilable compilingEngine = (Compilable) engine;
			
			Bindings bindings = new SimpleBindings();
			bindings.put("console", new DosisTilTekstWrapper().new ConsoleObject());
			engine.setBindings(bindings, ScriptContext.GLOBAL_SCOPE);
			
			script = compilingEngine.compile(javascriptFileReader);
			script.eval();
			
			combinedTextConverterObj = engine.eval("dosistiltekst.CombinedTextConverter");
			longTextConverterObj = engine.eval("dosistiltekst.Factory.getLongTextConverter()");
			shortTextConverterObj = engine.eval("dosistiltekst.Factory.getShortTextConverter()");
			dosageTypeCalculatorObj = engine.eval("dosistiltekst.DosageTypeCalculator");
			dosageTypeCalculator144Obj = engine.eval("dosistiltekst.DosageTypeCalculator144");
			dailyDosisCalculatorObj = engine.eval("dosistiltekst.DailyDosisCalculator");
			dosageProposalXMLGeneratorObj = engine.eval("dosistiltekst.DosageProposalXMLGenerator");
			invocable = (Invocable) engine;
		}
	}
	
	private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	public static DosageProposalResult getDosageProposalResult(String type, String iteration, String mapping, String unitTextSingular, String unitTextPlural, String supplementaryText, List<Date> beginDates, List<Date> endDates, FMKVersion version, int dosageProposalVersion) {
		return getDosageProposalResult(type, iteration, mapping, unitTextSingular, unitTextPlural, supplementaryText, beginDates, endDates, version, dosageProposalVersion, null);
	}
	
	public static DosageProposalResult getDosageProposalResult(String type, String iteration, String mapping, String unitTextSingular, String unitTextPlural, String supplementaryText, List<Date> beginDates, List<Date> endDates, FMKVersion version, int dosageProposalVersion, Integer shortTextMaxLength) {
		if(engine == null) {
			throw new RuntimeException("DosisTilTekstWrapper not initialized - call initialize() method before invoking any of the methods");
		}
				
		Map<String, Object> res;
		try {
			Context cx = Context.enter();
			Scriptable scope = cx.initStandardObjects();
			 
			ArrayList<Scriptable> nativeBeginDates = new ArrayList<Scriptable>(beginDates.size());
			ArrayList<Object> nativeEndDates = new ArrayList<Object>(endDates.size());
			 
			for(int i = 0; i < beginDates.size(); i++) {
				nativeBeginDates.add(cx.newObject(scope, "Date", new Object[] { new Double(beginDates.get(i).getTime()) }));
				nativeEndDates.add(endDates.get(i) != null ? cx.newObject(scope, "Date", new Object[] {  new Double(endDates.get(i).getTime()) })  : org.mozilla.javascript.Undefined.instance);
			}
			 
			
			NativeArray beginDateArray = new NativeArray(nativeBeginDates.toArray());
			NativeArray endDateArray = new NativeArray(nativeEndDates.toArray());
			 
			if(shortTextMaxLength != null) {
				res = (Map<String, Object>)invocable.invokeMethod(dosageProposalXMLGeneratorObj, "generateXMLSnippet", type, iteration, mapping, unitTextSingular, unitTextPlural, supplementaryText, beginDateArray, endDateArray, version.toString(), dosageProposalVersion, shortTextMaxLength.intValue());
			}
			else {
				res = (Map<String, Object>)invocable.invokeMethod(dosageProposalXMLGeneratorObj, "generateXMLSnippet", type, iteration, mapping, unitTextSingular, unitTextPlural, supplementaryText, beginDateArray, endDateArray, version.toString(), dosageProposalVersion);
			}
		} catch (ScriptException e) {
			e.printStackTrace();
			throw new RuntimeException("ScriptException in DosisTilTekstWrapper.getDosageProposalResult()", e);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new RuntimeException("NoSuchMethodException in DosisTilTekstWrapper.getDosageProposalResult()", e);
		} 

		Object shortTextObject = res.get("_shortDosageTranslation"); 
		String shortText = shortTextObject != null ? shortTextObject.toString() : null;
		Object longTextObject = res.get("_longDosageTranslation"); 
		String longText = longTextObject != null ? longTextObject.toString() : null;
		Object xmlObject = res.get("_xml");
		String xml = xmlObject != null ? xmlObject.toString() : null;
		
		return new DosageProposalResult(xml, shortText, longText);
	}
	
	public static DosageTranslationCombined convertCombined(DosageWrapper dosage) {

		if(engine == null) {
			throw new RuntimeException("DosisTilTekstWrapper not initialized - call initialize() method before invoking any of the methods");
		}
				
		String json = "(unset)";
		Map<String, Object> res;
		try {
			json = JSONHelper.toJsonString(dosage);
	        res = (Map<String, Object>)invocable.invokeMethod(combinedTextConverterObj, "convertStr", json);
		} catch (ScriptException e) {
			e.printStackTrace();
			throw new RuntimeException("ScriptException in DosisTilTekstWrapper.convertCombined() with json " + json, e);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new RuntimeException("NoSuchMethodException in DosisTilTekstWrapper.convertCombined() with json " + json, e);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException("JsonProcessingException inDosisTilTekstWrapper.convertCombined() with json " + json, e);
		}
		
		if(res == null) {
			return new DosageTranslationCombined(new DosageTranslation(null, null, new DailyDosis()), new LinkedList<DosageTranslation>());
		}
		
		Object combinedShortText = res.get("combinedShortText");
		Object combinedLongText = res.get("combinedLongText");
		
		DailyDosis combinedDD = getDailyDosisFromJS((Map<String, Object>)res.get("combinedDailyDosis"));
		NativeArray periodTexts = (NativeArray)res.get("periodTexts");
		
		LinkedList<DosageTranslation> translations = new LinkedList<DosageTranslation>(); 
		for(int i = 0; i < periodTexts.getLength(); i++) {
			NativeArray periodText = (NativeArray)periodTexts.get(i);
			translations.add(new DosageTranslation(periodText.get(0) != null ? periodText.get(0).toString() : null, periodText.get(1).toString(), getDailyDosisFromJS((Map<String, Object>)periodText.get(2))));
		}
		return new DosageTranslationCombined(new DosageTranslation(combinedShortText != null ? combinedShortText.toString() : null, combinedLongText.toString(), combinedDD), translations); 
	}
	
	public static String convertLongText(DosageWrapper dosage) {
		
		
		if(engine == null) {
			throw new RuntimeException("DosisTilTekstWrapper not initialized - call initialize() method before invoking any of the methods");
		}
		
		String json = "(unset)";
		Object res;
		try {
			json = JSONHelper.toJsonString(dosage);
	        res = invocable.invokeMethod(longTextConverterObj, "convertStr", json);

		} catch (ScriptException e) {
			e.printStackTrace();
			throw new RuntimeException("ScriptException in DosisTilTekstWrapper.convertLongText() with json " + json, e);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new RuntimeException("NoSuchMethodException in DosisTilTekstWrapper.convertLongText() with json " + json, e);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException("JsonProcessingException in DosisTilTekstWrapper.convertLongText() with json " + json, e);
		}
		
		return (String)res;
	}
	
	public static String convertShortText(DosageWrapper dosage) {
		
		if(engine == null) {
			throw new RuntimeException("DosisTilTekstWrapper not initialized - call initialize() method before invoking any of the methods");
		}
			
		
		Object res;
		String json = "(unset)";
		try {
			json = JSONHelper.toJsonString(dosage);
	        res = invocable.invokeMethod(shortTextConverterObj, "convertStr", json);
		} catch (ScriptException e) {
			e.printStackTrace();
			throw new RuntimeException("ScriptException in DosisTilTekstWrapper.convertShortText() with json " + json, e);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new RuntimeException("NoSuchMethodException in DosisTilTekstWrapper.convertShortText() with json " + json, e);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException("JsonProcessingException in DosisTilTekstWrapper.convertShortText() with json " + json, e);
		}
		
		return (String)res;
	}
	
	public static String convertShortText(DosageWrapper dosage, int maxLength) {
	
		if(engine == null) {
			throw new RuntimeException("DosisTilTekstWrapper not initialized - call initialize() method before invoking any of the methods");
		}
		
		String json = "(unset)";
		Object res;
		try {
			json = JSONHelper.toJsonString(dosage);
	        res = invocable.invokeMethod(shortTextConverterObj, "convertStr", json, maxLength);

		} catch (ScriptException e) {
			e.printStackTrace();
			throw new RuntimeException("ScriptException in DosisTilTekstWrapper.convertShortText() with json " + json, e);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new RuntimeException("NoSuchMethodException in DosisTilTekstWrapper.convertShortText() with json " + json, e);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException("JsonProcessingException in DosisTilTekstWrapper.convertShortText() with json " + json, e);
		}
		
		return (String)res;
	}
	
	public static String getShortTextConverterClassName(DosageWrapper dosage) {
		
		if(engine == null) {
			throw new RuntimeException("DosisTilTekstWrapper not initialized - call initialize() method before invoking any of the methods");
		}
		
		String json = "(unset)";
		Object res;
		try {
			json = JSONHelper.toJsonString(dosage);
			res = engine.eval("dosistiltekst.Factory.getShortTextConverter().getConverterClassName(" + json + ")");
		} catch (ScriptException e) {
			e.printStackTrace();
			throw new RuntimeException("ScriptException in getShortTextConverterClassName()", e);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException("JsonProcessingException in DosisTilTekstWrapper.getShortTextConverterClassName() with json " + json, e);
		}
		
		return (String)res;
	}
	
	public static String getLongTextConverterClassName(DosageWrapper dosage) {
		if(engine == null) {
			throw new RuntimeException("DosisTilTekstWrapper not initialized - call initialize() method before invoking any of the methods");
		}
		
		String json = "(unset)";
		Object res;
		try {
			json = JSONHelper.toJsonString(dosage);
			res = engine.eval("dosistiltekst.Factory.getLongTextConverter().getConverterClassName(" + json + ")");
		} catch (ScriptException e) {
			e.printStackTrace();
			throw new RuntimeException("ScriptException in getLongTextConverterClassName()", e);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException("JsonProcessingException in DosisTilTekstWrapper.getLongTextConverterClassName() with json " + json, e);
		}
		
		return (String)res;
	}
		
	public static DosageType getDosageType(DosageWrapper dosage) {
				
		if(engine == null) {
			throw new RuntimeException("DosisTilTekstWrapper not initialized - call initialize() method before invoking any of the methods");
		}
		
		String json = "(unset)";
		Object res;
		try {
			json = JSONHelper.toJsonString(dosage);
	        res = invocable.invokeMethod(dosageTypeCalculatorObj, "calculateStr", json);
		} catch (ScriptException e) {
			e.printStackTrace();
			throw new RuntimeException("ScriptException in getDosageType()", e);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new RuntimeException("NoSuchMethodException in getDosageType()", e);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException("JsonProcessingException in DosisTilTekstWrapper.getDosageType() with json " + json, e);
		}
		
		return DosageType.fromInteger(((Double)res).intValue());
	}
	
	public static DosageType getDosageType144(DosageWrapper dosage) {
		
		
		
		if(engine == null) {
			throw new RuntimeException("DosisTilTekstWrapper not initialized - call initialize() method before invoking any of the methods");
		}
		
		String json = "(unset)";
		Object res;
		try {
			json = JSONHelper.toJsonString(dosage);
	        res = invocable.invokeMethod(dosageTypeCalculator144Obj, "calculateStr", json);
		} catch (ScriptException e) {
			e.printStackTrace();
			throw new RuntimeException("ScriptException in getDosageType()", e);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new RuntimeException("NoSuchMethodException in getDosageType()", e);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException("JsonProcessingException in DosisTilTekstWrapper.getDosageType144() with json " + json, e);
		}
		
		return DosageType.fromInteger(((Double)res).intValue());
	}
	
	public static DailyDosis calculateDailyDosis(DosageWrapper dosage) {
		
		if(engine == null) {
			throw new RuntimeException("DosisTilTekstWrapper not initialized - call initialize() method before invoking any of the methods");
		}
		
		Map<String, Object> res;
		String json = "(unset)";
		
		try {
			json = JSONHelper.toJsonString(dosage);
	        res = (Map<String, Object>)invocable.invokeMethod(dailyDosisCalculatorObj, "calculateStr", json);
		} catch (ScriptException e) {
			e.printStackTrace();
			throw new RuntimeException("ScriptException in calculateDailyDosis()", e);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new RuntimeException("NoSuchMethodException in calculateDailyDosis()", e);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException("JsonProcessingException in DosisTilTekstWrapper.calculateDailyDosis() with json " + json, e);
		}
		
		return getDailyDosisFromJS(res);
	}
	
	private static DailyDosis getDailyDosisFromJS(Map<String, Object> res) {
		
		Map<String, Object> unitObject = (Map<String, Object>)res.get("unitOrUnits");
		if(unitObject == null) {
			return new DailyDosis();
		}
		UnitOrUnitsWrapper unitWrapper;
		if(unitObject.get("unit") != null) {
			unitWrapper = UnitOrUnitsWrapper.makeUnit((String)unitObject.get("unit"));
		}
		else {
			unitWrapper = UnitOrUnitsWrapper.makeUnits((String)unitObject.get("unitSingular"), (String)unitObject.get("unitPlural"));
		}
		Object value = res.get("value");
		if(value != null) {
			if(value instanceof Integer) {
				return new DailyDosis(BigDecimal.valueOf((Integer)value), unitWrapper);
			}
			else if(value instanceof Double) {
				return new DailyDosis(BigDecimal.valueOf((Double)value), unitWrapper);
			}
			else {
				throw new RuntimeException("Unexpected type of dailydosis value: " + value);
			}
		}
		else {
			Map<String, Object> interval = (Map<String, Object>)res.get("interval");
			return new DailyDosis(new Interval<BigDecimal>(BigDecimal.valueOf((Double)interval.get("minimum")), BigDecimal.valueOf((Double)interval.get("maximum"))), unitWrapper);
		}
	} 
}