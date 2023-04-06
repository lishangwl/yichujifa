package com.stardust.autojs.apkbuilder;

import java.io.File;
import java.io.Serializable;

public class KeyStore implements Serializable, Cloneable {
    private String certAlias;
    private String certPassword;
    private File file;
    private String password;

    public KeyStore(File file, String pass, String certAlias, String certPassword) {
        this.file = file;
        this.password = pass;
        this.certAlias = certAlias;
        this.certPassword = certPassword;
    }

    public File getFile() {
        return this.file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String cArr) {
        this.password = cArr;
    }

    public String getCertAlias() {
        return this.certAlias;
    }

    public void setCertAlias(String str) {
        this.certAlias = str;
    }

    public String getCertPassword() {
        return this.certPassword;
    }

    public void setCertPassword(String cArr) {
        this.certPassword = cArr;
    }
}