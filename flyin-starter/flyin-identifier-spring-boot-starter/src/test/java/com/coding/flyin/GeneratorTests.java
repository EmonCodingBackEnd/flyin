package com.coding.flyin;

import org.junit.Test;

import com.coding.flyin.starter.identifier.properties.ApplicationProperties;
import com.coding.flyin.starter.identifier.properties.ZookeeperProperties;
import com.coding.flyin.starter.identifier.generator.SnowflakeGenerator;
import com.coding.flyin.starter.identifier.register.zookeeper.ZookeeperMachineRegister;
import com.coding.flyin.starter.identifier.registry.zookeeper.ZookeeperRegistryCenter;

public class GeneratorTests {

    @Test
    public void test() {
        try {
            ZookeeperProperties zookeeperProperties = new ZookeeperProperties();
            zookeeperProperties.setConnectString("repo.emon.vip:2181");
            ZookeeperRegistryCenter registryCenter = new ZookeeperRegistryCenter(zookeeperProperties);

            ApplicationProperties applicationProperties = new ApplicationProperties();
            applicationProperties.setCacheable(false);
            applicationProperties.setPort(8080);
            applicationProperties.setDurable(true);
            applicationProperties.getDurableExtend().setNodeIdleToInvalidSeconds(300);
            applicationProperties.getDurableExtend().setNodeWarnThreshold(5);
            applicationProperties.getDurableExtend().setNodeCleanThreshold(10);
            applicationProperties.getDurableExtend().setNodeCleanIntervalSeconds(10);
            ZookeeperMachineRegister workerRegister =
                new ZookeeperMachineRegister(registryCenter, applicationProperties);

            SnowflakeGenerator generator = new SnowflakeGenerator(workerRegister);

            generator.init();
            System.out.println(generator.nextId());
            // generator.close();

            // 关闭后，无法再上报数据。
            registryCenter.close();
            System.out.println(generator.nextId());

            // 重连之后，可以再次上报数据
            registryCenter.init();
            System.out.println("dd");

            // generator.init();
            // System.out.println(generator.nextId());
            // generator.close();

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
