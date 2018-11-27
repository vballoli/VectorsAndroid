package in.swifiic.vec2;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import in.swifiic.vec2.helper.AckItem;
import in.swifiic.vec2.helper.Acknowledgement;

public class Vec2ExImLogic {

    private static final String TAG = "Vec2ExImLogic";

    //public static final String SRC_BASE="/sdcard/Vec2/src";

    public static final String PROJECT_BASE = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String SRC_BASE= PROJECT_BASE + "/Vec2/src";
    public static final String VECTORS_BASE= PROJECT_BASE + "/VectorsData";
    public static final String RCV_BASE= PROJECT_BASE + "/Vec2/rcv";
    public static final String RCV_BASE_JSON= PROJECT_BASE + "/Vec2/rcv_json";
    public static final String ACK_FILENAME = "ack_op_00";

    public static final double ADD_INCR=0.001;
    public static final double MULT_DECR=0.01;
    public static final int  Maxlayers=10;
    // Copy Count for MD + 10 layers
    public static final int CC_LIST[] = {16, 16, 14, 12, 10, 8, 4, 4, 4, 4, 4};

    private double persistentFactor = 1;
    private int burstInDT = 30;

    private Context context;

    public Vec2ExImLogic(Context context) {
        this.context = context;
    }

    private String getBaseFileNameForBurst(int burstId) {
        return "op_" + String.format("%06d", burstId) + "_4";
    }

    /**
     * Creates the .json file and moves it from SRC_BASE to MAIN_BASE
     *
     * @param fileName File to be moved.
     * @param copyCount
     * @param seqCount
     * @param originNode Current device node address
     * @param destNode Destination node address
     * @param ttlTime
     */
    private void createJsonAndMoveFiles(String fileName, int copyCount, int seqCount,
                                        String originNode, String destNode, int ttlTime) {
        Log.e(TAG, "createJsonAndMoveFiles: started " );
        Log.e(TAG, "createJsonAndMoveFiles: File: " + fileName );
        String jsonFile= fileName + ".json";
        long timeInSec = System.currentTimeMillis() / 1000;

        // WORKING here - need to populate the JSON string
        String outStr="{\"creationTime\":" + timeInSec +",\"fileName\":\"" + fileName+
                "\",\"maxSvcLayer\":2,\"maxTemporalLayer\":5,\"sequenceNumber\":" + seqCount +
                ",\"svcLayer\":0,\"temporalLayer\":0,\"tickets\":" + copyCount+
                ",\"traversal\":[{\"first\":"+timeInSec +",\"second\":\""+ originNode +
                "\"}],\"ttl\":" + ttlTime + ",\"destinationNode\":\""+destNode +
                "\",\"sourceNode\":\"" + originNode +"\"}";
        File f = new File(VECTORS_BASE +"/" + jsonFile );
        try {
            FileWriter fw = new FileWriter(f);
            fw.write(outStr);
            fw.close();
        } catch(Exception ex) {
            String msg = "Failed while creating JSON for filename " + jsonFile + " " + ex.getMessage();
            Log.e("JSON", msg);
            Toast.makeText(context, msg,  Toast.LENGTH_LONG).show();
        }
        try {
            File content = new File(SRC_BASE + "/" + fileName) ;
            File dest = new File(VECTORS_BASE + "/" + fileName);
            content.renameTo(dest);
        }catch(Exception exMove) {
            String msg = "Failed while moving filelename " + fileName + exMove.getMessage();

            Log.e("CONTENT", msg);
            Toast.makeText(context, msg,  Toast.LENGTH_LONG).show();
        }

        // move fileName from SRC_BASE to VECTORS_BASE
    }

    /**
     * Note the originNode and destNode are hardcoded 6 characters from Vectors App
     * for the respective devices. Code may be written something like
     * Query the setting Settings.Secure.ANDROID_ID to get androidId
     * and then take last 6 chars
     * String androidId = Settings.Secure.getString(VectorsApp.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
     * androidId.substring(androidId.length() - 6);
     * Alternatively vec2 may read it from Editable TextView
     * @param burstId Video count as numbers
     * @param destNode 6 digit string from Vectors.
     * @param originNode Current device nodeID.
     * @param delayTarget Time delay in seconds.
     */
    protected void exportBurst(int burstId, String originNode, String destNode, int delayTarget) {
        int filePresentCount =3;
        Log.e(TAG, "exportBurst: started ");
        // TODO get persistentFactor from sharedPreferences
        // ${indexLast}_L0T1.out|${indexSecondLast}_L0T1.out|${indexThirdLast}_L0T1.out
        String fileName1=VECTORS_BASE + getBaseFileNameForBurst( burstId -burstInDT)+"_L0T1.out";
        String fileName2=VECTORS_BASE + getBaseFileNameForBurst( burstId - 2 * burstInDT)+"_L0T1.out";
        String fileName3=VECTORS_BASE + getBaseFileNameForBurst( burstId - 4 * burstInDT)+"_L0T1.out";
        File f1 = new File(fileName1);
        File f2 = new File(fileName2);
        File f3 = new File(fileName3);
        if(f1.exists()) filePresentCount --;
        if(f2.exists()) filePresentCount --;
        if(f3.exists()) filePresentCount --;

        persistentFactor = persistentFactor * Math.pow(MULT_DECR, 3-filePresentCount) +
                ADD_INCR *filePresentCount;
        if(persistentFactor > 1)
            persistentFactor = 1;
        else if (persistentFactor < 1.0 / Maxlayers)
                persistentFactor = 1.0 / Maxlayers;


        // TODO store back persistentFactor in shared preference

        int numLayers = (int)(Maxlayers * persistentFactor);
        if(numLayers <1) numLayers =1;
        if(numLayers <10 && numLayers > 5) numLayers = 5;

        Log.e(TAG, "exportBurst: " + PROJECT_BASE );

        createJsonAndMoveFiles(getBaseFileNameForBurst(burstId) + ".md", CC_LIST[0], burstId, originNode, destNode, delayTarget * 2);

        for(int i =1; i <= numLayers && i <= 5; i++) {
            createJsonAndMoveFiles(getBaseFileNameForBurst(burstId) + "_L0T"+i + ".out", CC_LIST[i], burstId, originNode, destNode, delayTarget * 2);
        }
        if(numLayers ==10) {
            for(int i =1; i  <= 5; i++) {
                createJsonAndMoveFiles(getBaseFileNameForBurst(burstId) + "_L1T"+i + ".out", CC_LIST[i+5], burstId, originNode, destNode, delayTarget * 2);
            }

        }
    }

    /**
     *
     * now for destination logic
     *
     * https://github.com/swifiic/Vectors/blob/master/vectors/src/main/java/in/swifiic/vectors/helper/Acknowledgement.java
     * moves the file, backs up the JSON and creates a new ACK
     */
    private Acknowledgement getAckFromFile() {
        String ackFilename = ACK_FILENAME + ".json";
        try {
            String ackJSON = new Scanner(new File(VECTORS_BASE, ackFilename)).useDelimiter("\\Z").next();
            return Acknowledgement.fromString(ackJSON);
        } catch (Exception e) {
            Log.e(TAG, "Ack not found" + e.getMessage());
        }
        return null;
    }

    private void writeAckToFile(Acknowledgement ack) {
        String data = ack.toString();
        String ackFilename = ACK_FILENAME + ".json";
        try {
            File fWrite = new File(VECTORS_BASE, ackFilename);
            BufferedWriter writer = new BufferedWriter(new FileWriter(fWrite, false), 4096);
            writer.write(data);
            writer.close();
        } catch (Exception e) {
            Log.e(TAG, "Ack failed to write" + e.getMessage());
        }

    }

    public void importFilename(String filePathName) {
        Log.e(TAG, "importFilename: Started");
        File source = new File(filePathName);
        File sourceJson =  new File(filePathName + ".json");
        String fileName = source.getName();
        File dest = new File(RCV_BASE + "/" + fileName) ;
        File destJson = new File(RCV_BASE_JSON + "/" + fileName + ".json") ;
        try {
            source.renameTo(dest);
            Log.d(TAG, "importFilename: Success " + dest);
        }catch(Exception exMove) {
            String msg = "Failed while importing Content filelename " + fileName + exMove.getMessage();

            Log.e("CONTENT", msg);
            Toast.makeText(context, msg,  Toast.LENGTH_LONG).show();
        }
        try {
            sourceJson.renameTo(destJson);
            Log.d(TAG, "importFilename: Success JSON " + destJson);
        }catch(Exception exMove) {
            String msg = "Failed while importing Content filelename " + fileName + exMove.getMessage();

            Log.e("CONTENT", msg);
            Toast.makeText(context, msg,  Toast.LENGTH_LONG).show();
        }

        long timeInSec = System.currentTimeMillis() / 1000;
        AckItem  aI = new AckItem();
        aI.setTime(timeInSec);
        aI.setFilename(fileName);

        Acknowledgement ack = null;
        try {ack = getAckFromFile();} catch(Exception ex) {}
        if (ack != null) {
            ack.setAckTime(timeInSec);
        } else {
            ack = new Acknowledgement();
            ArrayList<AckItem> aIList = new ArrayList<>();
            ack.setItems(aIList);
            ack.setAckTime(timeInSec);
        }

        List<AckItem> itemList = ack.getItems();
        itemList.add(0,aI);
        if(itemList.size() > 2500)
            itemList.remove(2500);

        ack.setItems(itemList);

        writeAckToFile(ack);

    }
}

