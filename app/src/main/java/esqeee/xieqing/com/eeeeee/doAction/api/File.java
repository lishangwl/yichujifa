package esqeee.xieqing.com.eeeeee.doAction.api;

import com.xieqing.codeutils.util.FileIOUtils;
import com.xieqing.codeutils.util.FileUtils;
import com.xieqing.codeutils.util.HttpUtils;
import com.xieqing.codeutils.util.IntentUtils;
import com.xieqing.codeutils.util.ZipUtils;
import com.xieqing.codeutils.util.文件操作;
import com.yicu.yichujifa.GlobalContext;
import com.zhihu.matisse.compress.FileUtil;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import esqeee.xieqing.com.eeeeee.bean.JSONArrayBean;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.doAction.Base;
import esqeee.xieqing.com.eeeeee.library.MathResult;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;

public class File extends Base {
    public static final int ACTION_DELETE_FILE = 0;
    public static Base intansce;
    public static Base getIntansce(){
        if (intansce==null){
            intansce = new File();
        }
        return intansce;
    }

    @Override
    public boolean post(JSONBean param) {
        String arg = param.getString("arg");
        JSONBean var;
        switch (arg){
            case "删除文件":
                FileUtils.delete(getStringFromParam("path"));
                break;
            case "复制文件":
                FileUtils.copyFile(getStringFromParam("path"),getStringFromParam("topath"));
                break;
            case "重命名文件":
                FileUtils.rename(getStringFromParam("path"),getStringFromParam("topath"));
                break;
            case "删除目录":
                FileUtils.deleteDir(getStringFromParam("path"));
                break;
            case "创建目录":
                FileUtils.createOrExistsDir(getStringFromParam("path"));
                break;
            case "写出字节集文件":
                var = queryVariable(param.getString("var"));
                String path = getString(param.getString("path"));
                if (var == null){
                    RuntimeLog.e("<"+arg+">:错误，找不到变量["+param.getString("var")+"]");
                    return false;
                }
                writeByte(path,(byte[]) var.get("value"));
                break;
            case "写出文本文件":
                path = getString(param.getString("path"));
                boolean apped = param.getBoolean("append",false);
                String content = getString(param.getString("text"));
                String charest = getString(param.getString("chareset","UTF-8"));
                try {
                    FileOutputStream fout = new FileOutputStream(path,apped);
                    fout.write(content.getBytes(charest));
                    fout.close();
                } catch (Exception e) {
                    RuntimeLog.log(e);
                    return false;
                }
                break;
            case "读入字节集文件":
            case "读入文本文件":
                var = queryVariable(param.getString("var"));
                path = getString(param.getString("path"));
                if (var == null){
                    RuntimeLog.e("<"+arg+">:错误，找不到变量["+param.getString("var")+"]");
                    return false;
                }
                var.put("value",arg.equals("读入文本文件")?FileIOUtils.readFile2String(path,
                        getString(param.getString("chareset","UTF-8"))):FileIOUtils.readFile2BytesByStream(path));
                break;
            case "遍历目录":
                var = checkVarExiets(param,"var");
                path = getString(param.getString("path"));
                if (!FileUtils.isDir(path)){
                    RuntimeLog.i("不是目录，"+path);
                    return true;
                }else{
                    List<java.io.File> files = FileUtils.listFilesInDir(path);
                    String[] filePaths = new String[files.size()];
                    for (int i=0;i<files.size();i++){
                        filePaths[i] = files.get(i).getAbsolutePath();
                    }
                    setValue(var,new JSONArrayBean(filePaths));
                }
                break;
            case "文件是否存在":
                var = checkVarExiets(param,"var");
                setValue(var, FileUtil.isFileExists(getString(param.getString("text")))?"真":"假");
                break;
            case "获取网络文件":
                var = checkVarExiets(param,"var");
                MathResult result = evalMath(param.getString("time","5000"));
                if (result.isError()){
                    throw new RuntimeException("<error>计算表达式有误："+result.getException().getMessage());
                }
                int timeout = result.getResult();
                path = getStringFromParam("url");
                setValue(var, HttpUtils.GetToResponse(path).body().bytes());
                break;
            case "解压zip":
                path = getStringFromParam("path");
                String save = getStringFromParam("save");
                try {
                    ZipUtils.unzipFile(path,save);
                }catch (IOException e){
                    onError(e);
                }
                break;
            case "压缩zip":
                path = getStringFromParam("path");
                save = getStringFromParam("save");
                try {
                    ZipUtils.zipFile(path,save);
                }catch (IOException e){
                    onError(e);
                }
                break;
            case "打开文件":
                path = getStringFromParam("path");
                //文件操作.openFile(GboalContext.getContext(),path);
                //OpenFileUtils.open(GboalContext.getContext(),path);
                GlobalContext.getContext().startActivity(IntentUtils.getFileIntent(path));
                //TODO 打开文件 有问题
                break;
            case "取文件修改时间":
                var = checkVarExiets(param,"var");
                path = getStringFromParam("path");
                setValue(var, 文件操作.getFileLastModifyTime(path));
                break;
            case "取文件编码":
                var = checkVarExiets(param,"var");
                path = getStringFromParam("path");
                setValue(var, 文件操作.取文件编码(path));
                break;
            case "是否为隐藏文件":
                var = checkVarExiets(param,"var");
                path = getStringFromParam("path");
                setValue(var, 文件操作.是否为隐藏文件(path));
                break;
            case "取子目录":
                var = checkVarExiets(param,"var");
                path = getStringFromParam("path");
                setValue(var, 文件操作.取子目录(path));
                break;
            case "寻找文件关键词":
                var = checkVarExiets(param,"var");
                path = getStringFromParam("path");
                String keyword = getStringFromParam("keyword");
                setValue(var, 文件操作.寻找文件关键词(path,keyword));
                break;
            case "寻找文件后缀名":
                var = checkVarExiets(param,"var");
                path = getStringFromParam("path");
                String extension = getStringFromParam("extension");
                setValue(var, 文件操作.寻找文件后缀名(path,extension));
                break;
            case "调用播放器播放":
                path = getStringFromParam("path");
                文件操作.调用本地播放器(GlobalContext.getContext(),path);
                break;
        }
        return true;
    }


    private boolean writeByte(String file,byte[] bytes){
        try {
            java.io.File file1 = new java.io.File(file);
            FileOutputStream fileOutputStream = new FileOutputStream(file1);
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int getType() {
        return 2;
    }

    @Override
    public String getName() {
        return "文件操作";
    }
}
