/**
* The contents of this file are subject to the Mozilla Public
* License Version 1.1 (the "License"); you may not use this file
* except in compliance with the License. You may obtain a copy of
* the License at http://www.mozilla.org/MPL/
*
* Software distributed under the License is distributed on an "AS
* IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
* implied. See the License for the specific language governing
* rights and limitations under the License.
*
* Contributor(s): Contributors are attributed in the source code
* where applicable.
*
* The Original Code is "Dosis-til-tekst".
*
* The Initial Developer of the Original Code is Trifork Public A/S.
*
* Portions created for the FMK Project are Copyright 2011,
* National Board of e-Health (NSI). All Rights Reserved.
*/

package dk.medicinkortet.fmkdosistiltekstwrapper.ns20120601;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

import dk.medicinkortet.fmkdosistiltekstwrapper.DosageType;
import dk.medicinkortet.fmkdosistiltekstwrapper.rhino.DosisTilTekstWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.DosisTilTekstWrapperTestBase;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.DateOrDateTimeWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.DayWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.DosageWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.LocalTime;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.MorningDoseWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.PlainDoseWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.StructureWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.StructuresWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.TimedDoseWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.UnitOrUnitsWrapper;

/**
 * The purpose of this test class is to test new functionality added in FMK 1.4 (2012/06/01 namespace). 
 * The test of the general functionality is done in the testclass of the same name in the 
 * dk.medicinkortet.fmkdosistiltekstwrapper.ns2009 package. 
 */
public class LongTextComplexConverterTest extends DosisTilTekstWrapperTestBase {

	@Test 
	public void testUnits() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnits("tablet", "tabletter"), 
				StructureWrapper.makeStructure(
					1, null, DateOrDateTimeWrapper.makeDate("2012-04-18"), null,  
					DayWrapper.makeDay(
						1, 
						MorningDoseWrapper.makeDose(new BigDecimal(1), false)))));
		Assert.assertEquals(
				"Doseringsforløbet starter onsdag den 18. april 2012 og gentages hver dag:\n"+
				"   Doseringsforløb:\n"+
				"   1 tablet morgen", 
				DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals("1 tablet morgen", DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertEquals(
				1, 
				DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(), 
				0.000000001);
		Assert.assertEquals(DosageType.Fixed, DosisTilTekstWrapper.getDosageType(dosage));		
	}

	@Test 
	public void testAccordingToNeed() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnits("tablet", "tabletter"),
				StructureWrapper.makeStructure(
					0, null, DateOrDateTimeWrapper.makeDate("2012-04-18"), null,  
					DayWrapper.makeDay(
						0, 
						MorningDoseWrapper.makeDose(new BigDecimal(1), true)))));
		Assert.assertEquals(
				"Doseringsforløbet starter onsdag den 18. april 2012:\n"+
				"   Doseringsforløb:\n"+
				"   Efter behov: 1 tablet morgen efter behov", 
				DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals("1 tablet morgen efter behov (gentages ikke)", DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertNull(DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue());
		Assert.assertEquals(DosageType.AccordingToNeed, DosisTilTekstWrapper.getDosageType(dosage));		
	}

    @Test
    public void testTimes() throws Exception {
        DosageWrapper dosage = DosageWrapper.makeDosage(
                StructuresWrapper.makeStructures(
                        UnitOrUnitsWrapper.makeUnits("tablet", "tabletter"),
                        StructureWrapper.makeStructure(
                                0, null, DateOrDateTimeWrapper.makeDate("2012-04-18"), null,
                                DayWrapper.makeDay(
                                        0,
                                        TimedDoseWrapper.makeDose(new LocalTime(10,0), new BigDecimal(1), false)),
                                DayWrapper.makeDay(
                                        1,
                                        TimedDoseWrapper.makeDose(new LocalTime(10,1), new BigDecimal(2), false)),
                                DayWrapper.makeDay(
                                        2,
                                        TimedDoseWrapper.makeDose(new LocalTime(10,2,1), new BigDecimal(3), false)))));
        Assert.assertEquals(
                "Doseringsforløbet starter onsdag den 18. april 2012 og ophører efter det angivne forløb.\n" +
                        "Bemærk at doseringen varierer og har et komplekst forløb:\n" +
                        "   Doseringsforløb:\n" +
                        "   Dag ikke angivet: 1 tablet kl. 10:00\n" +
                        "   Onsdag den 18. april 2012: 2 tabletter kl. 10:01\n" +
                        "   Torsdag den 19. april 2012: 3 tabletter kl. 10:02:01",
                DosisTilTekstWrapper.convertLongText(dosage));
        Assert.assertNull(DosisTilTekstWrapper.convertShortText(dosage));
        Assert.assertNull(DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue());
        Assert.assertEquals(DosageType.Temporary, DosisTilTekstWrapper.getDosageType(dosage));
    }
    
    @Test
    public void testDifferentFirstDosage() throws Exception {
        DosageWrapper dosage = DosageWrapper.makeDosage(
            StructuresWrapper.makeStructures(
                UnitOrUnitsWrapper.makeUnits("tablet", "tabletter"),
                StructureWrapper.makeStructure(
                    0, null, DateOrDateTimeWrapper.makeDate("2012-04-18"), null,
                    DayWrapper.makeDay(
                        1,
                            TimedDoseWrapper.makeDose(new LocalTime(10,0), new BigDecimal(1), false),
                            
                            PlainDoseWrapper.makeDose(BigDecimal.valueOf(2)),
                            PlainDoseWrapper.makeDose(BigDecimal.valueOf(2)),
                            PlainDoseWrapper.makeDose(BigDecimal.valueOf(2)),
                            PlainDoseWrapper.makeDose(BigDecimal.valueOf(2))
                		))));
        Assert.assertEquals(
                "Doseringsforløbet starter onsdag den 18. april 2012 og ophører efter det angivne forløb:\n" +
                        "   Doseringsforløb:\n" +
                        "   Onsdag den 18. april 2012: 1 tablet kl. 10:00 + 2 tabletter 4 gange daglig",
                DosisTilTekstWrapper.convertLongText(dosage));
        Assert.assertNull(DosisTilTekstWrapper.convertShortText(dosage));
        Assert.assertEquals(9, DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().intValue());
        Assert.assertEquals(DosageType.Temporary, DosisTilTekstWrapper.getDosageType(dosage));
    }
    
    @Test
    public void testFirstDosageDiffersOnPN() throws Exception {
        DosageWrapper dosage = DosageWrapper.makeDosage(
            StructuresWrapper.makeStructures(
                UnitOrUnitsWrapper.makeUnits("tablet", "tabletter"),
                StructureWrapper.makeStructure(
                    0, null, DateOrDateTimeWrapper.makeDate("2012-04-18"), null,
                    DayWrapper.makeDay(
                        1,
                            PlainDoseWrapper.makeDose(BigDecimal.valueOf(2), true),
                            PlainDoseWrapper.makeDose(BigDecimal.valueOf(2), false),
                            PlainDoseWrapper.makeDose(BigDecimal.valueOf(2), false)
                		))));
        Assert.assertEquals(
                "Doseringsforløbet starter onsdag den 18. april 2012 og ophører efter det angivne forløb:\n" +
                        "   Doseringsforløb:\n" +
                        "   Onsdag den 18. april 2012: 2 tabletter efter behov + 2 tabletter 2 gange daglig",
                DosisTilTekstWrapper.convertLongText(dosage));
        Assert.assertNull(DosisTilTekstWrapper.convertShortText(dosage));
        Assert.assertNull(DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue());
        Assert.assertEquals(DosageType.Combined, DosisTilTekstWrapper.getDosageType(dosage));
    }
		
}