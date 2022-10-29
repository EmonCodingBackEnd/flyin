package com.coding.flyin.core.config;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.coding.flyin.core.config.thread.ThreadContextSlot;

public class FlyinConfigurerSupport implements ApplicationContextAware {

    static List<ThreadContextSlot> threadContextSlots = new ArrayList<>();
    static Map<Class<? extends Runnable>, List<ThreadContextSlot>> threadContextSlotsCache = new HashMap<>();

    public static List<ThreadContextSlot> getThreadContextSlot(Class<? extends Runnable> clazz, boolean asc) {
        Comparator<ThreadContextSlot> comparator = Comparator.comparing(ThreadContextSlot::getOrder);
        if (asc) {
            comparator = comparator.reversed();
        }
        if (threadContextSlotsCache.containsKey(clazz)) {
            List<ThreadContextSlot> matchedSlots = threadContextSlotsCache.get(clazz);
            matchedSlots = matchedSlots.stream().sorted(comparator).collect(Collectors.toList());
            return matchedSlots;
        }

        synchronized (FlyinConfigurerSupport.class) {
            List<ThreadContextSlot> matchedSlots = new ArrayList<>();
            for (ThreadContextSlot threadContextSlot : threadContextSlots) {
                if (threadContextSlot.supportRunnable(clazz)) {
                    matchedSlots.add(threadContextSlot);
                }
            }
            matchedSlots = matchedSlots.stream().sorted(comparator).collect(Collectors.toList());
            threadContextSlotsCache.put(clazz, matchedSlots);
        }

        return threadContextSlotsCache.get(clazz);
    }

    private static ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        applicationContext = ac;
        this.init();
    }

    private void init() {
        Map<String, FlyinConfigurer> threadContextSlotMap = applicationContext.getBeansOfType(FlyinConfigurer.class);
        for (Map.Entry<String, FlyinConfigurer> entry : threadContextSlotMap.entrySet()) {
            FlyinConfigurer flyinConfigurer = entry.getValue();
            flyinConfigurer.addThreadContextSlots(threadContextSlots);
        }
    }
}
