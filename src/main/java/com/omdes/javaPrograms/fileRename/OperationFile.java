package com.omdes.javaPrograms.fileRename;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: solitary.wang
 * Date: 2017/6/28
 * Time: 8:32
 */
public final class OperationFile implements Serializable {
    private static final long serialVersionUID = 4210320919211852349L;

    //选择重命名文件相关信息
    private String[] oldFileNames;
    private String oldFileName;
    private String[] sourcePaths;
    private String sourcePath;

    //重命名之后文件相关信息
    private String[] fileNames;
    private String fileName;
    private String destinationPaths;
    private String destinationPath;

    //重命名操作数据
    private int fileCount;
    private int pathCount;
    private int successCount;
    private int failedCount;

    //前缀
    private String prefix;

    //后缀
    private String suffix;
    private String[] suffixes;

    //重命名规则
    private int rule;
    private String ruleName;

    public String[] getOldFileNames() {
        return oldFileNames;
    }

    public void setOldFileNames(String[] oldFileNames) {
        this.oldFileNames = oldFileNames;
    }

    public String getOldFileName() {
        return oldFileName;
    }

    public void setOldFileName(String oldFileName) {
        this.oldFileName = oldFileName;
    }

    public String[] getSourcePaths() {
        return sourcePaths;
    }

    public void setSourcePaths(String[] sourcePaths) {
        this.sourcePaths = sourcePaths;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public String[] getFileNames() {
        return fileNames;
    }

    public void setFileNames(String[] fileNames) {
        this.fileNames = fileNames;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDestinationPaths() {
        return destinationPaths;
    }

    public void setDestinationPaths(String destinationPaths) {
        this.destinationPaths = destinationPaths;
    }

    public String getDestinationPath() {
        return destinationPath;
    }

    public void setDestinationPath(String destinationPath) {
        this.destinationPath = destinationPath;
    }

    public int getFileCount() {
        return fileCount;
    }

    public void setFileCount(int fileCount) {
        this.fileCount = fileCount;
    }

    public int getPathCount() {
        return pathCount;
    }

    public void setPathCount(int pathCount) {
        this.pathCount = pathCount;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public int getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(int failedCount) {
        this.failedCount = failedCount;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String[] getSuffixes() {
        return suffixes;
    }

    public void setSuffixes(String[] suffixes) {
        this.suffixes = suffixes;
    }

    public int getRule() {
        return rule;
    }

    public void setRule(int rule) {
        this.rule = rule;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("OperationFile").append("\n").
                append('{').append("\n").
                append("oldFileNames=[").append(StringUtils.join(oldFileNames, ",")).append("],\n").
                append("oldFileName='").append(oldFileName).append("\',\n").
                append("sourcePaths=[").append(StringUtils.join(sourcePaths, ",")).append("],\n").
                append("sourcePath='").append(sourcePath).append("\',\n").
                append("fileNames=[").append(StringUtils.join(fileNames, ",")).append("],\n").
                append("fileName='").append(fileName).append("\',\n").
                append("destinationPaths=[").append(destinationPaths).append("],\n").
                append("destinationPath='").append(destinationPath).append("\',\n").
                append("fileCount=").append(fileCount).append("\n").
                append("pathCount=").append(pathCount).append("\n").
                append("successCount=").append(successCount).append("\n").
                append("failedCount=").append(failedCount).append("\n").
                append("prefix='").append(prefix).append("\',\n").
                append("suffix='").append(suffix).append("\',\n").
                append("suffixes=[").append(StringUtils.join(suffixes, ",")).append("],\n").
                append("rule=").append(rule).append("\n").
                append("ruleName='").append(ruleName).append("\',\n").
                append('}');
        return sb.toString();
    }
}
