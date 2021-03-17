package com.coding.flyin.starter.timer.delay;

import java.io.Serializable;

/**
 * 延时任务抽象类.
 *
 * <p>创建时间: <font style="color:#00FFFF">20180516 10:07</font><br>
 * [请在此输入功能详述]
 *
 * @author Rushing0711
 * @version 1.0.0
 * @since 0.1.0
 */
public interface DelayTask extends Runnable, Serializable {

    /** 延时任务编号. */
    String getTaskId();
}
