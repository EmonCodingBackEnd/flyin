package com.coding.flyin.core.config;

import java.util.List;

import com.coding.flyin.core.config.thread.ThreadContextSlot;

public interface FlyinConfigurer {

    default void addThreadContextSlots(List<ThreadContextSlot> threadContextSlots) {}
}
