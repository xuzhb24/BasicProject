package com.android.util;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by xuzhb on 2020/2/4
 * Desc:Shell相关工具类，执行shell或root命令
 */
public class ShellUtil {

    //获取操作系统对应的换行符，如java中的\r\n，windows中的\r\n，linux/unix中的\r，mac中的\n
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    //执行shell命令
    public static CommandResult execShellCmd(String command) {
        return execCmd(new String[]{command}, false, true);
    }

    //执行root命令
    public static CommandResult execRootCmd(String command) {
        return execCmd(new String[]{command}, true, true);
    }

    public static CommandResult execCmd(String command, boolean isRoot) {
        return execCmd(new String[]{command}, isRoot, true);
    }

    public static CommandResult execCmd(String command, boolean isRoot, boolean isNeedResult) {
        return execCmd(new String[]{command}, isRoot, isNeedResult);
    }

    public static CommandResult execCmd(List<String> commands, boolean isRoot) {
        return execCmd(commands != null ? commands.toArray(new String[]{}) : null, isRoot, true);
    }

    public static CommandResult execCmd(List<String> commands, boolean isRoot, boolean isNeedResult) {
        return execCmd(commands != null ? commands.toArray(new String[]{}) : null, isRoot, isNeedResult);
    }

    public static CommandResult execCmd(String[] commands, boolean isRoot) {
        return execCmd(commands, isRoot, true);
    }

    /**
     * 执行Shell命令
     *
     * @param commands     命令数组
     * @param isRoot       是否需要root权限执行
     * @param isNeedResult 是否需要结果消息
     */
    public static CommandResult execCmd(String[] commands, boolean isRoot, boolean isNeedResult) {
        int code = -1;
        if (commands == null || commands.length == 0) {
            return new CommandResult(code, null, null);
        }
        Process process = null;
        BufferedReader successReader = null;
        BufferedReader failureReader = null;
        StringBuilder successMsg = null;
        StringBuilder failureMsg = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec(isRoot ? "su" : "sh");
            os = new DataOutputStream(process.getOutputStream());
            for (String command : commands) {
                if (TextUtils.isEmpty(command)) {
                    continue;
                }
                os.write(command.getBytes());
                os.writeBytes("\n");
                os.flush();
            }
            os.writeBytes("exit\n");
            os.flush();
            code = process.waitFor();
            if (isNeedResult) {
                successMsg = new StringBuilder();
                failureMsg = new StringBuilder();
                successReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
                failureReader = new BufferedReader(new InputStreamReader(process.getErrorStream(), "UTF-8"));
                String s;
                while ((s = successReader.readLine()) != null) {
                    successMsg.append(s).append(LINE_SEPARATOR);
                }
                while ((s = failureReader.readLine()) != null) {
                    failureMsg.append(s).append(LINE_SEPARATOR);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (process != null) {
                    process.destroy();
                }
                if (successReader != null) {
                    successReader.close();
                }
                if (failureReader != null) {
                    failureReader.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new CommandResult(
                code,
                successMsg != null ? successMsg.toString() : null,
                failureMsg != null ? failureMsg.toString() : null
        );
    }

    //返回的命令结果
    public static class CommandResult {

        private int code;           //结果码
        private String successMsg;  //成功信息
        private String failureMsg;  //失败信息

        public CommandResult(int code, String successMsg, String failureMsg) {
            this.code = code;
            this.successMsg = successMsg;
            this.failureMsg = failureMsg;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getSuccessMsg() {
            return successMsg;
        }

        public void setSuccessMsg(String successMsg) {
            this.successMsg = successMsg;
        }

        public String getFailureMsg() {
            return failureMsg;
        }

        public void setFailureMsg(String failureMsg) {
            this.failureMsg = failureMsg;
        }

        @Override
        public String toString() {
            return "CommandResult{" +
                    "code=" + code +
                    ", successMsg='" + successMsg + '\'' +
                    ", failureMsg='" + failureMsg + '\'' +
                    '}';
        }
    }

}
