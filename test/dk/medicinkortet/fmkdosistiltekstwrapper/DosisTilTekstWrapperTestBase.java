package dk.medicinkortet.fmkdosistiltekstwrapper;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.script.ScriptException;

import org.junit.Before;

import dk.medicinkortet.fmkdosistiltekstwrapper.rhino.DosisTilTekstWrapper;

public abstract class DosisTilTekstWrapperTestBase {

	static boolean isInitialized = false;
	
	@Before
	public void setUp() throws ScriptException, IOException {
		if(!isInitialized) {
			String jsLocation = System.getProperty("dosistiltekstJSlocation");
			if(jsLocation == null) {
				jsLocation = "../fmk-dosis-til-tekst-ts/target/dosistiltekst.js";	// For typical local developer use. Property value usually set on jenkins
			}
			DosisTilTekstWrapper.initialize(new FileReader(jsLocation));
			isInitialized = true;
		}
	}
}
