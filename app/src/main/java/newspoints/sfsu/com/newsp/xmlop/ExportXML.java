package newspoints.sfsu.com.newsp.xmlop;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;

import newspoints.sfsu.com.newsp_data.util.ProjectConstants;
import newspoints.sfsu.com.newsp_lib.util.ExportRecordingDetails;


public class ExportXML {

	Element root, child1, child2, child3, child4, child5, child6, child7, child8, child9;
	Text text;
	private Element child10;
	DocumentBuilderFactory dbfac;
	DocumentBuilder docBuilder;
	Document doc;
	SharedPreferences prefs;
	Editor edit;

	int UUID;
	int clipId = 0;
	int proejctTotalTime = 0;
	int lastClipEndTimeVideo = 0;

	int lastClipEndTimeAudio = 0;

	List<Element> audioFilesElements;

	public void createProjectXML(Context context, String totalTime) {
		try {
			// Creating an empty XML Document
			// We need a Document
			audioFilesElements = new ArrayList<>();
			if (prefs == null) {
				prefs = context.getSharedPreferences(context.getPackageName(),
						Context.MODE_PRIVATE);
				edit = prefs.edit();
			}

			dbfac = DocumentBuilderFactory.newInstance();
			docBuilder = dbfac.newDocumentBuilder();
			doc = docBuilder.newDocument();

			// Creating the XML tree

			// create the root element and add it to the document
			root = doc.createElement("sequence");
			root.setAttribute("id", "sequence-1");
			doc.appendChild(root);
			child1 = doc.createElement("uuid");
			root.appendChild(child1);
			UUID = prefs.getInt("UUID", 1);

			text = doc.createTextNode("" + UUID);
			edit.putInt("UUID", ++UUID);
			edit.commit();
			child1.appendChild(text);

			child1 = doc.createElement("duration");
			root.appendChild(child1);
			int totalTimeMin = Integer.parseInt(totalTime.substring(0, 2));
			int totalTimeSec = Integer.parseInt(totalTime.substring(3, 5));
			if (totalTimeMin > 0) {
				proejctTotalTime = totalTimeMin * 60 * 30;
			}
			if (totalTimeSec > 0) {
				proejctTotalTime = proejctTotalTime + (totalTimeSec * 30);
			}

			text = doc.createTextNode("" + proejctTotalTime);
			child1.appendChild(text);

			child1 = doc.createElement("rate");
			root.appendChild(child1);

			child2 = doc.createElement("timebase");
			text = doc.createTextNode("30");
			child2.appendChild(text);

			child1.appendChild(child2);
			child2 = doc.createElement("ntsc");
			text = doc.createTextNode("FALSE");
			child2.appendChild(text);
			child1.appendChild(child2);

			child1 = doc.createElement("name");
			text = doc.createTextNode(ProjectConstants.selectedProjectName);
			child1.appendChild(text);
			root.appendChild(child1);

			child1 = doc.createElement("media");
			root.appendChild(child1);
			child2 = doc.createElement("video");
			child1.appendChild(child2);
			child3 = doc.createElement("format");
			child2.appendChild(child3);

			child4 = doc.createElement("samplecharacteristics");
			child3.appendChild(child4);

			child5 = doc.createElement("rate");
			child4.appendChild(child5);
			// everything to be added in child 4 till ie sample characteristics

			child6 = doc.createElement("timebase");
			text = doc.createTextNode("30");
			child6.appendChild(text);
			child5.appendChild(child6);

			child7 = doc.createElement("ntsc");
			text = doc.createTextNode("FALSE");
			child7.appendChild(text);
			child5.appendChild(child7);

			child5 = doc.createElement("codec");
			child4.appendChild(child5);

			// add childer of code ie childs of parent named child5 codec
			child6 = doc.createElement("name");
			text = doc.createTextNode("Apple ProRes 422");
			child6.appendChild(text);
			child5.appendChild(child6);

			child6 = doc.createElement("Appspecificdata");
			// add childerm of child6 here ie appspecific data
			child5.appendChild(child6);
			child7 = doc.createElement("appname");
			text = doc.createTextNode("Final Cut Pro");
			child7.appendChild(text);
			child6.appendChild(child7);

			child7 = doc.createElement("appmanufacturer");
			text = doc.createTextNode("Apple Inc.");
			child7.appendChild(text);
			child6.appendChild(child7);

			child7 = doc.createElement("appversion");
			text = doc.createTextNode("7.0");
			child7.appendChild(text);
			child6.appendChild(child7);

			child7 = doc.createElement("data");
			child6.appendChild(child7);

			// Add children of child7 ie .. data

			child8 = doc.createElement("qtcodec");
			child7.appendChild(child8);
			// Add children of child8 ie .. qtcodec

			child9 = doc.createElement("codecname");
			text = doc.createTextNode("Apple ProRes 422");
			child9.appendChild(text);
			child8.appendChild(child9);

			child9 = doc.createElement("codectypename");
			text = doc.createTextNode("Apple ProRes 422");
			child9.appendChild(text);
			child8.appendChild(child9);

			child9 = doc.createElement("codectypecode");
			text = doc.createTextNode("apcn");
			child9.appendChild(text);
			child8.appendChild(child9);

			child9 = doc.createElement("codecvendorcode");
			text = doc.createTextNode("appl");
			child9.appendChild(text);
			child8.appendChild(child9);

			child9 = doc.createElement("spatialquality");
			text = doc.createTextNode("1024");
			child9.appendChild(text);
			child8.appendChild(child9);

			child9 = doc.createElement("temporalquality");
			text = doc.createTextNode("0");
			child9.appendChild(text);
			child8.appendChild(child9);

			child9 = doc.createElement("keyframerate");
			text = doc.createTextNode("0");
			child9.appendChild(text);
			child8.appendChild(child9);

			child9 = doc.createElement("datarate");
			text = doc.createTextNode("0");
			child9.appendChild(text);
			child8.appendChild(child9);

			// Again adding child of CODEC child 4 ie samplecharacteristics

			child5 = doc.createElement("width");
			text = doc.createTextNode("1920");
			child5.appendChild(text);
			child4.appendChild(child5);

			child5 = doc.createElement("height");
			text = doc.createTextNode("1080");
			child5.appendChild(text);
			child4.appendChild(child5);

			child5 = doc.createElement("anamorphic");
			text = doc.createTextNode("0");
			child5.appendChild(text);
			child4.appendChild(child5);

			child5 = doc.createElement("pixelaspectratio");
			text = doc.createTextNode("square");
			child5.appendChild(text);
			child4.appendChild(child5);

			child5 = doc.createElement("fielddominance");
			text = doc.createTextNode("none");
			child5.appendChild(text);
			child4.appendChild(child5);

			child5 = doc.createElement("colordepth");
			text = doc.createTextNode("24");
			child5.appendChild(text);
			child4.appendChild(child5);

			child3 = doc.createElement("track");
			child3.setAttribute("TL.SQTrackExpandedHeight", "100");
			child3.setAttribute("TL.SQTrackExpanded", "1");
			child2.appendChild(child3);

			File categoryNamePath = new File(
					Environment.getExternalStorageDirectory() + "/journalist/"
							+ ProjectConstants.selectedProjectName);
			String[] categoryListTemp = categoryNamePath.list();

			String categoryName = categoryListTemp[0];
			List<String> selectionArg = new ArrayList<String>();
			Set<String> temp = prefs.getStringSet(categoryName + "shot", null);
			try {
				Iterator<String> j = temp.iterator();

				System.out.println("Calling for the ids of shjot tupe");
				for (int i = 0; i < temp.size(); i++) {
					String tt = (String) j.next();
					String yy = prefs.getString(tt, "000");
					selectionArg.add(yy);
				}
			} catch (Exception e) {

			}
			lastClipEndTimeVideo = 0;
			lastClipEndTimeAudio = 0;
			clipId = 1;
			ArrayList<ExportRecordingDetails> shotFilesDetails = ProjectConstants.dbClass.getShotFilesDetails(selectionArg,
							ProjectConstants.selectedProjectName);
			for (int i = 0; i < shotFilesDetails.size(); i++) {
				child10 = AddVideoTag(shotFilesDetails.get(i),
						shotFilesDetails.size(), clipId);
				clipId++;
				child3.appendChild(child10);
			}

			child10 = createAudioTags();
			child1.appendChild(child10);
			// child1.setAttribute("name", "value");
			// root.appendChild(child1);

			// create a comment and put it in the root element
			// Comment comment = doc.createComment("Just a thought");
			// root.appendChild(comment);

			// create child element, add an attribute, and add to root
			// child1 = doc.createElement("child");
			// child1.setAttribute("name", "value");
			// root.appendChild(child1);

			// add a text element to the child

			// ///////////////
			// Output the XML

			// set up a transformer
			TransformerFactory transfac = TransformerFactory.newInstance();
			Transformer trans = transfac.newTransformer();
			trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			trans.setOutputProperty(OutputKeys.INDENT, "yes");

			// create string from xml tree
			StringWriter sw = new StringWriter();
			StreamResult result = new StreamResult(sw);
			DOMSource source = new DOMSource(doc);
			trans.transform(source, result);

			String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE xmeml> "
					+ "<xmeml version=\"4\">" + sw.toString() + "<\\xmeml";

			String fpath = Environment.getExternalStorageDirectory()
					.getAbsolutePath()
					+ "/journalist/"
					+ ProjectConstants.selectedProjectName
					+ "/"
					+ ProjectConstants.selectedProjectCategory
					+ "/Export"
					+ ".xml";
			File file = new File(fpath);
			System.out.println("file path is *********** " + fpath);
			// If file does not exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(xmlString);
			bw.close();
			// print xml
			System.out.println("Here's the xml \n" + xmlString);
		} catch (Exception e) {
			System.out.println("EXCEPTION OCCURES" + e);
		}
	}

	private Element createAudioTags() {
		child4 = doc.createElement("audio");
		child5 = doc.createElement("numOutputChannels");
		text = doc.createTextNode("" + 2);
		child5.appendChild(text);
		child4.appendChild(child5);

		child5 = doc.createElement("format");
		child4.appendChild(child5);
		child6 = doc.createElement("samplecharacteristics");
		child5.appendChild(child6);

		child7 = doc.createElement("depth");
		text = doc.createTextNode("16");
		child7.appendChild(text);
		child6.appendChild(child7);

		child7 = doc.createElement("samplerate");
		text = doc.createTextNode("48000");
		child7.appendChild(text);
		child6.appendChild(child7);

		// child5 = doc.createElement("track");
		// child5.setAttribute("TL.SQTrackExpandedHeight", "60");
		// child5.setAttribute("TL.SQTrackExpanded", "1");
		// child5.setAttribute("TL.currentExplodedTrackIndex", "0");
		// child5.setAttribute("TL.totalExplodedTrackCount", "2");
		// child5.setAttribute("TL.premiereTrackType", "Stereo");

		// child4.appendChild(child5);

		for (int i = 0; i < audioFilesElements.size(); i++) {
			child4.appendChild(audioFilesElements.get(i));
		}

		return child4;
	}

	private Element createRelatedAudiotags(ExportRecordingDetails shotId,
			int noOfClips, int audioClipId, String recordingId, int clipId,
			int trackIndex) {
		Element childAudio;
		childAudio = doc.createElement("track");
		childAudio.setAttribute("TL.SQTrackExpandedHeight", "60");
		childAudio.setAttribute("TL.SQTrackExpanded", "1");
		childAudio.setAttribute("TL.currentExplodedTrackIndex", "0");
		childAudio.setAttribute("TL.totalExplodedTrackCount", "2");
		childAudio.setAttribute("TL.premiereTrackType", "Stereo");

		child4 = doc.createElement("clipitem");

		child4.setAttribute("id", "clipitem-" + audioClipId);

		child4.setAttribute("frameBlend", "FALSE");
		child4.setAttribute("premiereTrackType", "Stereo");
		// child3.appendChild(child4);
		// childer being added to clipitemid ie child4
		childAudio.appendChild(child4);

		child5 = doc.createElement("masterclipid");
		text = doc.createTextNode("masterclip-" + recordingId);
		child5.appendChild(text);
		child4.appendChild(child5);
		child5 = doc.createElement("name");
		text = doc.createTextNode("NPVideo Recording " + recordingId + ".mp4");
		child5.appendChild(text);
		child4.appendChild(child5);

		child5 = doc.createElement("enabled");
		text = doc.createTextNode("TRUE");
		child5.appendChild(text);
		child4.appendChild(child5);

		child5 = doc.createElement("duration");
		// TODO: get duration
		int duration = ProjectConstants.dbClass
				.getVideoRecordingDuration(shotId.recordingId);
		duration = duration * 30;
		text = doc.createTextNode("" + duration);

		child5.appendChild(text);
		child4.appendChild(child5);

		child4.appendChild(createRateNode());

		child5 = doc.createElement("start");
		System.out.println("STARTM TIEM OS " + lastClipEndTimeAudio);
		if (lastClipEndTimeAudio == 0) {
			text = doc.createTextNode("0");

		} else {
			text = doc.createTextNode("" + lastClipEndTimeAudio);

		}
		child5.appendChild(text);

		child4.appendChild(child5);

		int clipLength = (shotId.duration * 30);

		System.out.println("STARTM TIEM OS " + shotId.duration
				+ " Clip length " + clipLength);
		System.out.println("Clip length is " + shotId.duration + "   "
				+ clipLength);
		lastClipEndTimeAudio = lastClipEndTimeAudio + clipLength;
		child5 = doc.createElement("end");
		text = doc.createTextNode("" + lastClipEndTimeAudio);
		child5.appendChild(text);
		child4.appendChild(child5);

		long recordingStartPoint = ProjectConstants.dbClass
				.getStartPointInClip(shotId.shotId, shotId.recordingId);
		recordingStartPoint = recordingStartPoint * 30;
		child5 = doc.createElement("in");
		text = doc.createTextNode("" + recordingStartPoint);
		child5.appendChild(text);
		child4.appendChild(child5);

		child5 = doc.createElement("out");
		long recordingEndPoint = recordingStartPoint + clipLength;
		text = doc.createTextNode("" + recordingEndPoint);
		child5.appendChild(text);
		child4.appendChild(child5);
		text = doc.createTextNode("");

		Long pproMultiiplyFactor = 8467200000l;
		child5 = doc.createElement("pproTicksIn");
		long pproIn = recordingStartPoint * pproMultiiplyFactor;
		text = doc.createTextNode("0");
		child5.appendChild(text);
		child4.appendChild(child5);

		long pproOut = recordingEndPoint * pproMultiiplyFactor;
		child5 = doc.createElement("pproTicksOut");
		text = doc.createTextNode("" + pproOut);
		child5.appendChild(text);
		child4.appendChild(child5);

		child5 = doc.createElement("file");
		child5.setAttribute("id", "file-" + shotId.recordingId);
		child4.appendChild(child5);

		child6 = doc.createElement("sourcetrack");
		// child6.appendChild(text);
		child4.appendChild(child6);

		child7 = doc.createElement("mediatype");
		text = doc.createTextNode("audio");
		child7.appendChild(text);
		child6.appendChild(child7);

		child7 = doc.createElement("trackindex");
		text = doc.createTextNode("" + trackIndex);
		child7.appendChild(text);
		child6.appendChild(child7);
		//
		// child6 = doc.createElement("sourcetrack");
		// child6.appendChild(text);
		// child5.appendChild(child6);

		child4.appendChild(createLinkTagVideo(noOfClips, clipId));

		audioClipId = (2 * clipId) + noOfClips - 1;

		for (int i = 0; i < 2; i++) {
			child4.appendChild(createLinkTagAudio(noOfClips, clipId,
					audioClipId, 3 + i));
			++audioClipId;

		}
		child5 = doc.createElement("logginginfo");
		child4.appendChild(child5);

		child6 = doc.createElement("description");
		child5.appendChild(child6);

		child6 = doc.createElement("scene");
		child5.appendChild(child6);

		child6 = doc.createElement("shottake");
		child5.appendChild(child6);

		child6 = doc.createElement("lognote");
		child5.appendChild(child6);

		child5 = doc.createElement("labels");
		child4.appendChild(child5);

		child6 = doc.createElement("label2");
		text = doc.createTextNode("Iris");
		child6.appendChild(text);
		child5.appendChild(child6);

		child6 = doc.createElement("enabled");
		text = doc.createTextNode("TRUE");
		child6.appendChild(text);
		childAudio.appendChild(child6);

		child6 = doc.createElement("locked");
		text = doc.createTextNode("FALSE");
		child6.appendChild(text);
		childAudio.appendChild(child6);

		child6 = doc.createElement("outputchannelindex");
		text = doc.createTextNode("1");
		child6.appendChild(text);
		childAudio.appendChild(child6);

		return childAudio;

	}

	private Element AddVideoTag(ExportRecordingDetails shotId, int noOfClips,
								int clipId) {
		// TODO Auto-generated method stub

		// childer being added to Track ie child3
		int audioClipId = (2 * clipId) + noOfClips - 1;

		audioFilesElements.add(createRelatedAudiotags(shotId, noOfClips,
				audioClipId, shotId.recordingId, clipId, 1));
		audioClipId++;
		audioFilesElements.add(createRelatedAudiotags(shotId, noOfClips,
				audioClipId, shotId.recordingId, clipId, 2));

		child4 = doc.createElement("clipitem");

		child4.setAttribute("id", "clipitem-" + clipId);

		child4.setAttribute("frameBlend", "FALSE");

		// child3.appendChild(child4);
		// childer being added to clipitemid ie child4

		child5 = doc.createElement("masterclipid");
		text = doc.createTextNode("masterclip-" + shotId.recordingId);
		child5.appendChild(text);
		child4.appendChild(child5);
		int storeNewClipId = clipId + 1;
		edit.putInt(ProjectConstants.selectedProjectName + "ClipId",
				storeNewClipId);
		edit.commit();

		child5 = doc.createElement("name");
		text = doc.createTextNode(shotId.shotId);
		child5.appendChild(text);
		child4.appendChild(child5);

		child5 = doc.createElement("enabled");
		text = doc.createTextNode("TRUE");
		child5.appendChild(text);
		child4.appendChild(child5);

		child5 = doc.createElement("duration");
		System.out.println(":Looking for duration" + shotId.shotId
				+ " REcpgoinm id " + shotId.recordingId);
		int duration = ProjectConstants.dbClass
				.getVideoRecordingDuration(shotId.recordingId);
		duration = duration * 30;
		text = doc.createTextNode("" + duration);

		child5.appendChild(text);
		child4.appendChild(child5);

		child4.appendChild(createRateNode());

		child5 = doc.createElement("start");
		if (lastClipEndTimeVideo == 0) {
			text = doc.createTextNode("0");

		} else {
			text = doc.createTextNode("" + lastClipEndTimeVideo);

		}
		child5.appendChild(text);

		child4.appendChild(child5);

		int clipLength = (shotId.duration * 30);
		System.out.println("Clip length is " + shotId.duration + "   "
				+ clipLength);
		lastClipEndTimeVideo = lastClipEndTimeVideo + clipLength;
		child5 = doc.createElement("end");
		text = doc.createTextNode("" + lastClipEndTimeVideo);
		child5.appendChild(text);
		child4.appendChild(child5);

		long recordingStartPoint = ProjectConstants.dbClass
				.getStartPointInClip(shotId.shotId, shotId.recordingId);
		recordingStartPoint = recordingStartPoint * 30;
		child5 = doc.createElement("in");
		text = doc.createTextNode("" + recordingStartPoint);
		child5.appendChild(text);
		child4.appendChild(child5);

		child5 = doc.createElement("out");
		long recordingEndPoint = recordingStartPoint + clipLength;
		text = doc.createTextNode("" + recordingEndPoint);
		child5.appendChild(text);
		child4.appendChild(child5);
		text = doc.createTextNode("");
		Long pproMultiiplyFactor = 8467200000l;
		child5 = doc.createElement("pproTicksIn");
		long pproIn = recordingStartPoint * pproMultiiplyFactor;
		text = doc.createTextNode("0");
		child5.appendChild(text);
		child4.appendChild(child5);
		long pproOut = recordingEndPoint * pproMultiiplyFactor;

		child5 = doc.createElement("pproTicksOut");
		text = doc.createTextNode("" + pproOut);
		child5.appendChild(text);
		child4.appendChild(child5);

		child5 = doc.createElement("alphatype");
		text = doc.createTextNode("black");
		child5.appendChild(text);
		child4.appendChild(child5);

		child5 = doc.createElement("pixelaspectratio");
		text = doc.createTextNode("square");
		child5.appendChild(text);
		child4.appendChild(child5);

		child5 = doc.createElement("anamorphic");
		text = doc.createTextNode("FALSE");
		child5.appendChild(text);
		child4.appendChild(child5);

		child4.appendChild(createFileIDElement(
				Integer.parseInt(shotId.recordingId), duration));
		child4.appendChild(createLinkTagVideo(noOfClips, clipId));
		System.out.println(":link video tag added");
		audioClipId = (2 * clipId) + noOfClips - 1;

		for (int i = 0; i < 2; i++) {
			child4.appendChild(createLinkTagAudio(noOfClips, clipId,
					audioClipId, 3 + i));
			++audioClipId;

		}
		child5 = doc.createElement("logginginfo");
		child4.appendChild(child5);

		child6 = doc.createElement("description");
		child5.appendChild(child6);

		child6 = doc.createElement("scene");
		child5.appendChild(child6);

		child6 = doc.createElement("shottake");
		child5.appendChild(child6);

		child6 = doc.createElement("lognote");
		child5.appendChild(child6);

		child6 = doc.createElement("labels");
		child4.appendChild(child6);

		child7 = doc.createElement("labels2");
		text = doc.createTextNode("Iris");
		child7.appendChild(text);
		child6.appendChild(child7);

		return child4;
	}

	private Element createLinkTagVideo(int noOfClips, int clipId) {

		System.out.println("Clip id sent is " + clipId);
		Element child1, child2, child3, child4, child5;
		child1 = doc.createElement("link");

		child2 = doc.createElement("linkclipref");
		text = doc.createTextNode("clipitem-" + clipId);
		child2.appendChild(text);
		child1.appendChild(child2);

		child2 = doc.createElement("mediatype");
		text = doc.createTextNode("video");
		child2.appendChild(text);
		child1.appendChild(child2);

		child2 = doc.createElement("trackindex");

		text = doc.createTextNode("" + clipId);
		child2.appendChild(text);
		child1.appendChild(child2);

		child2 = doc.createElement("clipindex");
		text = doc.createTextNode("1");
		child2.appendChild(text);
		child1.appendChild(child2);

		return child1;
	}

	private Element createLinkTagAudio(int noOfClips, int clipId,
			int audioClipId, int trackIndex) {

		System.out.println("Clip id sent is " + clipId);
		Element child1, child2, child3, child4, child5;
		child1 = doc.createElement("link");

		child2 = doc.createElement("linkclipref");
		text = doc.createTextNode("clipitem-" + audioClipId);
		child2.appendChild(text);
		child1.appendChild(child2);

		child2 = doc.createElement("mediatype");
		text = doc.createTextNode("audio");
		child2.appendChild(text);
		child1.appendChild(child2);

		child2 = doc.createElement("trackindex");

		text = doc.createTextNode("" + trackIndex);
		child2.appendChild(text);
		child1.appendChild(child2);

		child2 = doc.createElement("clipindex");
		text = doc.createTextNode("" + clipId);
		child2.appendChild(text);
		child1.appendChild(child2);

		child2 = doc.createElement("groupindex");
		text = doc.createTextNode("1");
		child2.appendChild(text);
		child1.appendChild(child2);

		return child1;
	}

	// private Node createDurationDetailsNode() {
	// // TODO Auto-generated method stub
	//
	// Element child1, child2, child3;
	//
	// child1 = doc.createElement("start");
	// text =
	// doc.createTextNode("{starting position, in frames, of clip on timeline}");
	// child1.appendChild(text);
	//
	//
	// return null;
	// }

	// TODO Auto-generated method stub
	private Node createFileIDElement(int recordingId, int recordingDuration) {

		Element child1, child2, child3, child4, child5;

		child1 = doc.createElement("file");
		child1.setAttribute("id", "file- " + recordingId);

		child2 = doc.createElement("name");
		text = doc.createTextNode("NPVideo Recording " + recordingId + ".mp4");
		child2.appendChild(text);
		child1.appendChild(child2);

		child2 = doc.createElement("pathurl");
		text = doc.createTextNode("Assests/" + "NPVideo Recording " + recordingId
				+ ".mp4");
		child2.appendChild(text);
		child1.appendChild(child2);

		child1.appendChild(createRateNode());
		child2 = doc.createElement("Duration");
		text = doc.createTextNode("" + recordingDuration);
		child2.appendChild(text);

		child1.appendChild(child2);

		// child1.appendChild(createRateNode());
		child2 = doc.createElement("timecode");
		child1.appendChild(child2);

		child2.appendChild(createRateNode());

		child3 = doc.createElement("string");
		text = doc.createTextNode("00:00:00:00");
		child3.appendChild(text);
		child2.appendChild(child3);

		child3 = doc.createElement("frame");
		text = doc.createTextNode("0");
		child3.appendChild(text);
		child2.appendChild(child3);

		child3 = doc.createElement("displayformat");
		text = doc.createTextNode("NDF");
		child3.appendChild(text);
		child2.appendChild(child3);

		child3 = doc.createElement("reel");
		child2.appendChild(child3);

		child4 = doc.createElement("name");
		child3.appendChild(child4);

		child2 = doc.createElement("media");
		child1.appendChild(child2);

		child3 = doc.createElement("video");
		child2.appendChild(child3);

		child4 = doc.createElement("samplecharacteristics");
		child3.appendChild(child4);

		child4.appendChild(createRateNode());

		child5 = doc.createElement("width");
		text = doc.createTextNode("1980");
		child5.appendChild(text);
		child4.appendChild(child5);

		child5 = doc.createElement("height");
		text = doc.createTextNode("1020");
		child5.appendChild(text);
		child4.appendChild(child5);

		child5 = doc.createElement("anamorphic");
		text = doc.createTextNode("FALSE");
		child5.appendChild(text);
		child4.appendChild(child5);

		child5 = doc.createElement("pixelaspectratio");
		text = doc.createTextNode("square");
		child5.appendChild(text);
		child4.appendChild(child5);

		child5 = doc.createElement("fielddominance");
		text = doc.createTextNode("none");
		child5.appendChild(text);
		child4.appendChild(child5);

		child3 = doc.createElement("audio");
		child2.appendChild(child3);

		child4 = doc.createElement("samplecharacteristics");
		child3.appendChild(child4);

		child5 = doc.createElement("depth");
		text = doc.createTextNode("16");
		child5.appendChild(text);
		child4.appendChild(child5);

		child5 = doc.createElement("samplerate");
		text = doc.createTextNode("48000");
		child5.appendChild(text);
		child4.appendChild(child5);

		child4 = doc.createElement("channelcount");
		text = doc.createTextNode("2");
		child4.appendChild(text);
		child3.appendChild(child4);

		return child1;
	}

	private Element createRateNode() {
		Element child1, child2, child3;
		child1 = doc.createElement("rate");
		root.appendChild(child1);

		child2 = doc.createElement("timebase");
		text = doc.createTextNode("30");
		child2.appendChild(text);

		child1.appendChild(child2);
		child2 = doc.createElement("ntsc");
		text = doc.createTextNode("FALSE");
		child2.appendChild(text);
		child1.appendChild(child2);
		return child1;
	}
}
