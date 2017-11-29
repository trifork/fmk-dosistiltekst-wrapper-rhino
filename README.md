fmk-dosistiltekst-wrapper-rhino
==============
fmk-dosistiltekst-wrapper-rhino udstiller et java API der indkapsler dosistiltekst javascript komponenten (se https://www.npmjs.com/package/fmk-dosis-til-tekst-ts ). Denne version understøtter java 6 og 7, der alle anvender den såkalde "Rhino" javascript fortolker, deraf komponent-navnet. (For java 8 henvises til fmk-dosistiltekst-wrapper-nashorn)

Komponenten kan hentes i binær udgave (som jar-fil) inkl. dependencies her: https://github.com/trifork/FMKResources/blob/master/fmk-dosistiltekst-wrapper-rhino.zip

JS komponenten selv kan hentes vha. "npm i fmk-dosis-til-tekst-ts". Javascriptfilen dosistiltekst.js findes herefter i node_modules/fmk-dosis-til-tekst-ts/target folderen.

Før brug skal DosisTilTekstWrapper klassen initialiseres med en java FileStream indeholdende dosistiltekst.js filen, eksempelvis:
```java
DosisTilTekstWrapper.initialize(new FileReader("node_modules/fmk-dosis-til-tekst-ts/target/dosistiltekst.js"));
```

Herefter kan de respektive hjælpe-metoder anvendes. De kaldes alle med en DosageWrapper instans som argument. DosageWrapper klasserne er identiske med de tidligere wrapper-klasser kendt fra den gamle dosis-til-tekst komponent, kun med ændret namespace. Det burde derfor være en forholdsvis enkel kodeopgave at skifte fra den gamle komponent til den nye, vha. dette projekt.

### Doseringsoversættelse

Eksempel på anvendelse, doseringsoversættelse:

```java
DosageWrapper dosage = DosageWrapper.makeDosage(
  StructuresWrapper.makeStructures(
    UnitOrUnitsWrapper.makeUnits("tablet", "tabletter"), 
      StructureWrapper.makeStructure(
        1, "ved måltid", 
        DateOrDateTimeWrapper.makeDate("2011-01-01"), DateOrDateTimeWrapper.makeDate("2011-01-30"), 
        DayWrapper.makeDay(1, 
          PlainDoseWrapper.makeDose(new BigDecimal(1)), 
          PlainDoseWrapper.makeDose(new BigDecimal(1)), 
          PlainDoseWrapper.makeDose(new BigDecimal(1), true)))));
  
String longText = DosisTilTekstWrapper.convertLongText(dosage);
String shortText = DosisTilTekstWrapper.convertShortText(dosage);
String mediumText = DosisTilTekstWrapper.convertShortText(dosage, 200);
DailyDosis daily = DosisTilTekstWrapper.calculateDailyDosis(dosage);
DosageType dosageType = DosisTilTekstWrapper.getDosageType(dosage);
```
Desuden er der også mulighed for at hente kort og lang tekst + daglig dosis hhv. kombineret for fler-periode strukturerede doseringer, samt for hver enkelt periode:
```java
DosageTranslationCombined combined = DosisTilTekstWrapper.convertCombined(dosage);
```
### XML generering ud fra doseringsforslag

Eksempel på anvendelse:
```java
import dk.medicinkortet.fmkdosistiltekstwrapper.FMKVersion;
...
SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

DosageProposalResult res = DosisTilTekstWrapper.getDosageProposalResult("PN", "1", "1", "tablet", "tabletter", ", tages med rigeligt vand", Arrays.asList(SIMPLE_DATE_FORMAT.parse("2017-05-17")), Arrays.asList(SIMPLE_DATE_FORMAT.parse("2017-06-01")), FMKVersion.FMK146, 1);

String xml = res.getXmlSnippet();
String longText = res.getLongText();
String shortText = res.getShortText();
```
Eksempel på anvendelse med flere doseringsperioder:
```
DosageProposalResult res = DosisTilTekstWrapper.getDosageProposalResult("{M+M+A+N}{PN}{N daglig}", "{1}{2}{1}",
				"{1+2+3+4}{dag 1: 2 dag 2: 3}{2}", "tablet", "tabletter", "tages med rigeligt vand",
				Arrays.asList(SIMPLE_DATE_FORMAT.parse("2010-01-01"), SIMPLE_DATE_FORMAT.parse("2010-02-01"), SIMPLE_DATE_FORMAT.parse("2010-03-01")),
				Arrays.asList(SIMPLE_DATE_FORMAT.parse("2010-01-31"), SIMPLE_DATE_FORMAT.parse("2010-02-28"), SIMPLE_DATE_FORMAT.parse("2010-03-31")),
				FMKVersion.FMK146, 1);
```				

## Kun til udviklere af selve fmk-dosistiltekst-wrapper komponenten:
Forudsætter fmk-dosis-til-tekst-ts er checket ud og bygget "parallelt" med dette projekt, således at ../fmk-dosis-til-tekst-ts/target/dosistiltekst.js er tilgængelig.