package esqeee.xieqing.com.eeeeee.action;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.thl.filechooser.FileInfo;
import com.xieqing.codeutils.util.AppUtils;
import com.xieqing.codeutils.util.EncryptUtils;
import com.xieqing.codeutils.util.FileIOUtils;
import com.xieqing.codeutils.util.FileUtils;
import com.xieqing.codeutils.util.StringUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import esqeee.xieqing.com.eeeeee.bean.JSONArrayBean;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.doAction.DoActionBean;
import esqeee.xieqing.com.eeeeee.fragment.ActionListFragment;

public class ActionHelper {

    private static String TAG = ActionHelper.class.getSimpleName();

    public final static File projectDir = new File(Environment.getExternalStorageDirectory() + "/YiChuJiFaProject");
    public final static File saveDir = new File(projectDir, "save");
    public final static File shareDir = new File(projectDir, "share");
    public final static File buildDir = new File(projectDir, "build");
    public final static File workSpaceDir = new File(projectDir, "workSpace");
    public final static File downDir = new File(workSpaceDir, "下载");
    public final static File simpleDir = new File(workSpaceDir, "使用示例");
    public final static File workSpaceImageDir = new File(projectDir, "workSpaceImage");


    /*
     *   是否工作目录
     * */
    public static boolean isRootWorkDir(File currentDir) {
        if (!currentDir.exists() || !currentDir.isDirectory()) {
            return false;
        }
        return currentDir.getAbsolutePath().equals(workSpaceDir.getAbsolutePath());
    }

    /*
     *   搜索目录下所有脚本和子目录
     *   @param directoty -- 待被搜索的目录
     * */
    public static List<File> searchAllActionAndDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files == null) {
            return new ArrayList<>();
        }
        List<File> allDirectory = new ArrayList<>();
        List<File> allFile = new ArrayList<>();
        List<File> all = new ArrayList<>();
        for (File file : files) {
            if (file != null && file.exists()) {
                if (file.isDirectory()) {
                    allDirectory.add(file);
                    continue;
                }
                if (isActionFile(file)) {
                    allFile.add(file);
                }
            }
        }

        Comparator name = new Comparator<File>() {
            @Override
            public int compare(File fileInfo, File t1) {
                return fileInfo.getName().toLowerCase().compareTo(t1.getName().toLowerCase());
            }
        };
        Collections.sort(allDirectory, name);
        Collections.sort(allFile, name);

        all.addAll(allDirectory);
        all.addAll(allFile);
        return all;
    }

    /*
     *   搜索目录下所有脚本和子目录
     *   @param directoty -- 待被搜索的目录
     * */
    public static List<File> searchAllXmlAndDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files == null) {
            return new ArrayList<>();
        }
        List<File> allDirectory = new ArrayList<>();
        List<File> allFile = new ArrayList<>();
        List<File> all = new ArrayList<>();
        for (File file : files) {
            if (file != null && file.exists()) {
                if (file.isDirectory()) {
                    allDirectory.add(file);
                    continue;
                }
                if (isXmlFile(file)) {
                    allFile.add(file);
                }
            }
        }

        Comparator name = new Comparator<File>() {
            @Override
            public int compare(File fileInfo, File t1) {
                return fileInfo.getName().toLowerCase().compareTo(t1.getName().toLowerCase());
            }
        };
        Collections.sort(allDirectory, name);
        Collections.sort(allFile, name);

        all.addAll(allDirectory);
        all.addAll(allFile);
        return all;
    }

    /*
     *   搜索目录下所有脚本和子目录
     *   @param directoty -- 待被搜索的目录
     * */
    public static List<File> searchAllActionAndDirectoryAndXml(File directory) {
        File[] files = directory.listFiles();
        if (files == null) {
            return new ArrayList<>();
        }
        List<File> allDirectory = new ArrayList<>();
        List<File> allFile = new ArrayList<>();
        List<File> all = new ArrayList<>();
        for (File file : files) {
            if (file != null && file.exists()) {
                if (file.isDirectory()) {
                    allDirectory.add(file);
                    continue;
                }
                if (isActionFile(file) || isXmlFile(file)) {
                    allFile.add(file);
                }
            }
        }

        Comparator name = new Comparator<File>() {
            @Override
            public int compare(File fileInfo, File t1) {
                return fileInfo.getName().toLowerCase().compareTo(t1.getName().toLowerCase());
            }
        };
        Collections.sort(allDirectory, name);
        Collections.sort(allFile, name);

        all.addAll(allDirectory);
        all.addAll(allFile);
        return all;
    }

    /*
     * 是否是YCF文件
     * */
    public static boolean isXmlFile(File file) {
        return FileUtils.getFileExtension(file).equals("ycfml");
    }

    /*
     * 是否是YCF文件
     * */
    public static boolean isActionFile(File file) {
        return FileUtils.getFileExtension(file).equals("ycf");
    }


    /*
     *   获取指令中所有用到的图
     */
    public static List<String> getAllImages(Action action) {
        List<String> get = new ArrayList<>();
        getAllImages(new JSONBean(action.getData()).getArray("actions"), get);
        return get;
    }


    /*
     *   获取指令中所有用到的图
     */
    private static void getAllImages(JSONArrayBean array, List<String> get) {
        if (array == null) {
            return;
        }
        for (int i = 0; i < array.length(); i++) {
            JSONBean jsonObject = array.getJson(i);
            int type = jsonObject.getInt("actionType");
            JSONBean param = jsonObject.getJson("param");
            JSONArrayBean conditions = jsonObject.getArray("conditions");
            if (conditions == null) {
                conditions = new JSONArrayBean();
            }
            JSONBean trueDo = jsonObject.getJson("trueDo");
            if (trueDo == null) {
                trueDo = new JSONBean();
            }
            JSONBean falseDo = jsonObject.getJson("falseDo");
            if (falseDo == null) {
                falseDo = new JSONBean();
            }
            switch (DoActionBean.getBeanFromType(type)) {
                case CONDITION_SCREEN_IMAGE:
                case CONDITION_RECT_IMAGE:
                case LONG_CLICK_IMAGE:
                case LONG_CLICK_IMAGE_RECT:
                case CLICK_IMAGE_RECT:
                case CLICK_IMAGE:
                    get.add(param.getString("fileName"));
                    break;
                case IF:
                    for (int b = 0; b < conditions.length(); b++) {
                        JSONBean condition = conditions.getJson(b);
                        switch (condition.getInt("actionType")) {
                            case 47:
                            case 48:
                                get.add(condition.getJson("param").getString("fileName"));
                                break;
                        }
                    }

                    if (trueDo.has("actions")) {
                        getAllImages(trueDo.getArray("actions"), get);
                    }
                    if (falseDo.has("actions")) {
                        getAllImages(falseDo.getArray("actions"), get);
                    }
                    break;
                case WHILE:
                    for (int b = 0; b < conditions.length(); b++) {
                        JSONBean condition = conditions.getJson(b);
                        switch (condition.getInt("actionType")) {
                            case 47:
                            case 48:
                                get.add(condition.getJson("param").getString("fileName"));
                                break;
                        }
                    }
                    if (trueDo.has("actions")) {
                        getAllImages(trueDo.getArray("actions"), get);
                    }
                    break;
                case FOR:
                    if (trueDo.has("actions")) {
                        getAllImages(trueDo.getArray("actions"), get);
                    }
                    break;
            }
        }
    }

    /*
     *   Action To String
     *   @param withImage - is put used image to base64 encode String
     * */

    public static String actionToString(Action action, boolean withImage, boolean isEncrypt) {
        if (action != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<title>").append(action.getTitle()).append("</title>\n");
            stringBuilder.append("<repeat>").append(action.getRepeat()).append("</repeat>\n");
            stringBuilder.append("<icon>").append(action.getIcon()).append("</icon>\n");
            stringBuilder.append("<count>").append(action.getCount()).append("</count>\n");
            stringBuilder.append("<log>").append(action.getLog()).append("</log>\n");
            stringBuilder.append("<data>").append(URLEncoder.encode(action.getData() == null ? "" : action.getData())).append("</data>\n");
            stringBuilder.append("<status>").append(action.getStatus()).append("</status>\n");
            stringBuilder.append("<type>").append(action.getType()).append("</type>\n");
            if (withImage) {
                List<String> images = getAllImages(action);
                for (String image : images) {
                    File file = new File(image);
                    if (!file.exists()) {
                        continue;
                    }
                    stringBuilder.append("<image>").append("\n");
                    stringBuilder.append("<name>").append(file.getName()).append("</name>\n");
                    byte[] datas = FileIOUtils.readFile2BytesByChannel(file);
                    String encode = datas == null ? "" : datas.length == 0 ? "" : Base64.encodeToString(datas, 0);
                    //String encode = datas == null ? "" : datas.length == 0 ? "" : Base64EncodeTwo(datas);
                    stringBuilder.append("<data>").append(encode).append("</data>\n");
                    stringBuilder.append("</image>\n");
                }
            }
            String encrypt = stringBuilder.toString();
            if (isEncrypt) {
                try {
                    //encrypt = "encrypt=a" + Base64.encodeToString(encrypt.getBytes(), 0);
                    encrypt = "encrypt=a" + Base64EncodeTwo(encrypt.getBytes());
                } catch (Exception e) {
                }
            }
            return encrypt;
        } else {
            return "";
        }
    }

    /**
     * base64编码俩次
     *
     * @param datas
     * @return
     */
    public static String Base64EncodeTwo(byte[] datas) {
        byte[] encode = Base64.encode(datas, 0);
        encode = Base64.encode(encode, 0);
        return new String(encode);
    }

    /**
     * base64解码俩次
     *
     * @param datas
     * @return
     */
    public static String Base64DncodeTwo(byte[] datas) {
        byte[] decode = Base64.decode(datas, 0);
        decode = Base64.decode(decode, 0);
        return new String(decode);
    }

    public static String decrypt(String decrypt) {
        Log.d(TAG, "decrypt:" + decrypt);
        try {
            if (decrypt.startsWith("encrypt")) {
                String decrypt1 = new String(Base64.decode(decrypt.replace("encrypt=a", "").getBytes(), 0));
                if (!decrypt1.contains("<title>")) {//如果解密一次失败，就解密2次
                    Log.d(TAG, "decrypt:解密2次");
                    String decrypt2 = Base64DncodeTwo(decrypt.replace("encrypt=a", "").getBytes());
                    Log.d(TAG, "decrypt:" + decrypt2);
                    return decrypt2;
                } else {
                    Log.d(TAG, "decrypt:解密1次");
                    Log.d(TAG, "decrypt:" + decrypt1);
                    return decrypt1;
                }
            }
            return decrypt;
        } catch (Exception e) {
            e.printStackTrace();
            return decrypt;
        }
    }

    /*
     *   Action To String
     *   @param withImage - is put used image to base64 encode String
     * */

    public static String actionToString(Action action, boolean withImage) {
        return actionToString(action, withImage, false);
    }


    /*
     *   Action To String
     * */

    public static String actionToString(Action action) {
        return actionToString(action, false);
    }


    /*
     *  String To Action
     * */
    public static Action stringToAction(String encrypt) {
        return stringToAction(encrypt, false);
    }


    /*
     *  String To Action
     *  @param isImportImage -- is put used image to file
     * */
    public static Action stringToAction(String encrypt, boolean isImportImage) {
        Action action = new Action();
        try {
            String decrypt = encrypt;
            if (decrypt.startsWith("encrypt")) {
                action.setEncrypt(true);
                //decrypt = new String(Base64.decode(decrypt.replace("encrypt=a", "").getBytes(), 0));
                decrypt = Base64DncodeTwo(decrypt.replace("encrypt=a", "").getBytes());
            }
            String log = StringUtils.getSubString(decrypt, "<log>", "</log>");
            if (TextUtils.isEmpty(log)) {
                action.setLog(0);
            } else {
                action.setLog(Integer.parseInt(log));
            }
            action.setIcon(StringUtils.getSubString(decrypt, "<icon>", "</icon>"));
            action.title = (StringUtils.getSubString(decrypt, "<title>", "</title>"));
            action.setRepeat(Integer.parseInt(StringUtils.getSubString(decrypt, "<repeat>", "</repeat>")));
            action.setCount(Integer.parseInt(StringUtils.getSubString(decrypt, "<count>", "</count>")));
            action.setStatus(Integer.parseInt(StringUtils.getSubString(decrypt, "<status>", "</status>")));
            action.setType(Integer.parseInt(StringUtils.getSubString(decrypt, "<type>", "</type>")));
            action.setData(URLDecoder.decode(StringUtils.getSubString(decrypt, "<data>", "</data>")));
            action.setTime(System.currentTimeMillis());
            if (isImportImage) {
                String[] images = StringUtils.getSubStringArray(decrypt, "<image>", "</image>");
                for (String image : images) {
                    File file = new File(ActionHelper.workSpaceImageDir, StringUtils.getSubString(image, "<name>", "</name>"));
                    if (file.exists()) {
                        continue;
                    }
                    byte[] data = Base64.decode(StringUtils.getSubString(image, "<data>", "</data>"), 0);
                    FileIOUtils.writeFileFromBytesByChannel(file, data, true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return action;
    }


    /*
     *  parse a Action from file path
     */
    public static Action from(String path) {
        if (path == null) {
            return null;
        }
        File file = new File(path);
        return from(file);
    }

    /*
     *  parse a Action from file
     */
    public static Action from(File file) {
        if (file.exists()) {
            Action action = ActionHelper.stringToAction(FileIOUtils.readFile2String(file), false);
            if (action != null) {
                action.setPath(file.getAbsolutePath());
            }
            return action;
        }
        return null;
    }

    public static void importTo(String f, String s) {
        Log.d("ActionHelper", "importTo f:" + f + ",s:" + s);
        File file = new File(f);
        if (FileUtils.isDir(file)) {
            List<File> files =
                    searchAllActionAndDirectory(file);
            FileUtils.createOrExistsDir(s + "/" + file.getName());
            for (File file1 : files) {
                importTo(file1.getAbsolutePath(), s + "/" + file.getName());
            }
        } else {
            if (!isActionFile(file)) {
                return;
            }
            Action action = stringToAction(FileIOUtils.readFile2String(file), true);

            if (action != null) {
                String title = action.getTitle();
                if (TextUtils.isEmpty(title)) {
                    String string = FileIOUtils.readFile2String(file);
                    if (string.contains("encrypt=a")) {
                        string = ActionHelper.decrypt(string);
                    }
                    title = StringUtils.getSubString(string, "<title>", "</title>");
                }
                action.setPath(s + "/" + title + ".ycf");
                action.save();
                Log.d("ActionHelper", "importTo " + s + "/" + title + ".ycf");
            }
        }

    }
}
