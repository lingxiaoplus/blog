package com.lingxiao.blog.utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;
import oshi.SystemInfo;
import oshi.hardware.*;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class SystemUtil {
    private static final SystemInfo SYSTEM_INFO = new SystemInfo();

    private DecimalFormat mFormat = new DecimalFormat("#.##%");
    public CpuInfo getCpuInfo() throws InterruptedException {
        //System.out.println("----------------cpu信息----------------");
        CentralProcessor processor = SYSTEM_INFO.getHardware().getProcessor();
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        // 睡眠1s
        TimeUnit.SECONDS.sleep(1);
        long[] ticks = processor.getSystemCpuLoadTicks();
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long cSys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;

        log.debug("----------------cpu信息----------------");
        log.debug("cpu核数:{}",processor.getLogicalProcessorCount());
        log.debug("cpu系统使用率:{}",mFormat.format(cSys * 1.0 / totalCpu));
        log.debug("cpu用户使用率:{}",mFormat.format(user * 1.0 / totalCpu));
        log.debug("cpu当前等待率:{}",mFormat.format(iowait * 1.0 / totalCpu));
        log.debug("cpu当前使用率:{}",mFormat.format(1.0 - (idle * 1.0 / totalCpu)));
        CpuInfo cpuInfo = new CpuInfo();
        cpuInfo.setCoreNum(processor.getLogicalProcessorCount());
        cpuInfo.setSystemUsage(mFormat.format(cSys * 1.0 / totalCpu));
        cpuInfo.setUserUsage(mFormat.format(user * 1.0 / totalCpu));
        cpuInfo.setWaitRate(mFormat.format(iowait * 1.0 / totalCpu));
        cpuInfo.setUsageRate(mFormat.format(1.0 - (idle * 1.0 / totalCpu)));
        return cpuInfo;
    }

    public MemoryInfo getMemInfo() {
        System.out.println("----------------主机内存信息----------------");
        GlobalMemory memory = SYSTEM_INFO.getHardware().getMemory();
        //总内存
        long totalByte = memory.getTotal();
        //剩余
        long acaliableByte = memory.getAvailable();

        System.out.println("总内存 = " + formatByte(totalByte));
        System.out.println("使用" + formatByte(totalByte - acaliableByte));
        System.out.println("剩余内存 = " + formatByte(acaliableByte));
        System.out.println("使用率：" + mFormat.format((totalByte - acaliableByte) * 1.0 / totalByte));

        MemoryInfo memoryInfo = new MemoryInfo();
        memoryInfo.setTotal(formatByte(totalByte));
        memoryInfo.setUsed(formatByte(totalByte - acaliableByte));
        memoryInfo.setAcaliable(formatByte(acaliableByte));
        memoryInfo.setUsageRate(mFormat.format((totalByte - acaliableByte) * 1.0 / totalByte));
        return memoryInfo;
    }

    public DiskStoreInfo getDiskStore(){
        /*HWDiskStore[] diskStores = SYSTEM_INFO.getHardware().getDiskStores();
        long totalSize = 0L;
        for (HWDiskStore diskStore : diskStores) {
            long size = diskStore.getSize();
            String name = diskStore.getName();
            long writeBytes = diskStore.getWriteBytes();

            HWPartition[] partitions = diskStore.getPartitions();
            log.debug("名字：{}，大小：{},reads: {}",name,formatByte(size),formatByte(writeBytes));
            totalSize += size;
            for (HWPartition partition : partitions) {
                String partitionName = partition.getName();
                long partitionSize = partition.getSize();
                log.debug("名字2：{}，大小2：{}",partitionName,formatByte(partitionSize));
            }
        }*/
        long freeSize = 0L;
        long usedSize = 0L;
        long totalSize = 0L;
        File[] disks = File.listRoots();
        for(File file : disks)
        {
            freeSize += file.getFreeSpace();
            usedSize += file.getUsableSpace();
            totalSize += file.getTotalSpace();
        }
        DiskStoreInfo diskStoreInfo = new DiskStoreInfo();
        diskStoreInfo.setUsed(formatByte(usedSize));
        diskStoreInfo.setAcaliable(formatByte(freeSize));
        diskStoreInfo.setTotal(formatByte(totalSize));
        diskStoreInfo.setUsageRate(mFormat.format(usedSize*1.0/totalSize));
        return diskStoreInfo;
    }

    public String getOsName(){
        Properties props = System.getProperties();
        //系统名称
        String osName = props.getProperty("os.name");
        return osName;
    }
    public String getOsArch(){
        Properties props = System.getProperties();
        //系统名称
        String osArch = props.getProperty("os.arch");
        return osArch;
    }


    public JvmInfo getJvmInfo() {
        System.out.println("----------------jvm信息----------------");
        Properties props = System.getProperties();
        Runtime runtime = Runtime.getRuntime();
        //jvm总内存
        long jvmTotalMemoryByte = runtime.totalMemory();
        //jvm最大可申请
        long jvmMaxMoryByte = runtime.maxMemory();
        //空闲空间
        long freeMemoryByte = runtime.freeMemory();
        //jdk版本
        String jdkVersion = props.getProperty("java.version");
        //jdk路径
        String jdkHome = props.getProperty("java.home");
        System.out.println("jvm内存总量 = " + formatByte(jvmTotalMemoryByte));
        System.out.println("jvm已使用内存 = " + formatByte(jvmTotalMemoryByte - freeMemoryByte));
        System.out.println("jvm剩余内存 = " + formatByte(freeMemoryByte));
        System.out.println("jvm内存使用率 = " + mFormat.format((jvmTotalMemoryByte - freeMemoryByte) * 1.0 / jvmTotalMemoryByte));
        System.out.println("java版本 = " + jdkVersion);
        //System.out.println("jdkHome = " + jdkHome);

        JvmInfo jvmInfo = new JvmInfo();
        jvmInfo.setTotal(formatByte(jvmTotalMemoryByte));
        jvmInfo.setUsed(formatByte(jvmTotalMemoryByte - freeMemoryByte));
        jvmInfo.setAcaliable(formatByte(freeMemoryByte));
        jvmInfo.setUsageRate(mFormat.format((jvmTotalMemoryByte - freeMemoryByte) * 1.0 / jvmTotalMemoryByte));
        jvmInfo.setVersion(jdkVersion);
        return jvmInfo;
    }

    public void getThread() {
        System.out.println("----------------线程信息----------------");
        ThreadGroup currentGroup = Thread.currentThread().getThreadGroup();

        while (currentGroup.getParent() != null) {
            // 返回此线程组的父线程组
            currentGroup = currentGroup.getParent();
        }
        //此线程组中活动线程的估计数
        int noThreads = currentGroup.activeCount();

        Thread[] lstThreads = new Thread[noThreads];
        //把对此线程组中的所有活动子组的引用复制到指定数组中。
        currentGroup.enumerate(lstThreads);
        for (Thread thread : lstThreads) {
            System.out.println("线程数量：" + noThreads + " 线程id：" + thread.getId() + " 线程名称：" + thread.getName() + " 线程状态：" + thread.getState());
        }
    }


    private NetworkData networkData = new NetworkData();
    public NetworkData getNetworkState(){
        NetworkIF[] networkIFs = SYSTEM_INFO.getHardware().getNetworkIFs();
        BlockingQueue<String> xAxis = networkData.getXAxis();
        BlockingQueue<Double> series = networkData.getSeries();
        for (NetworkIF networkIF : networkIFs) {
            long bytesSent = networkIF.getPacketsSent();
            double y = bytesSent/1024.0;
            if (y == 0){
                break;
            }
            DateTime dateTime = new DateTime(networkIF.getTimeStamp());
            String x = dateTime.toString("HH:mm:ss");

            log.debug("时间：{},速度：{} /秒",x,y);
            if (series.size() >= 8){
                if (series.poll() != null){
                    series.offer(y);
                    log.debug("删除series元素：{}",series);
                }
            }else {
                series.offer(y);
            }

            if (xAxis.size() >= 8){
                if (xAxis.poll() != null){
                    xAxis.offer(x);
                    log.debug("删除xAxis元素：{}",xAxis);
                }
            }else {
                xAxis.offer(x);
            }

        }
        return networkData;
    }

    public String formatByte(long byteNumber) {
        //换算单位
        double FORMAT = 1024.0;
        double kbNumber = byteNumber / FORMAT;
        if (kbNumber < FORMAT) {
            return new DecimalFormat("#.##KB").format(kbNumber);
        }
        double mbNumber = kbNumber / FORMAT;
        if (mbNumber < FORMAT) {
            return new DecimalFormat("#.##MB").format(mbNumber);
        }
        double gbNumber = mbNumber / FORMAT;
        if (gbNumber < FORMAT) {
            return new DecimalFormat("#.##GB").format(gbNumber);
        }
        double tbNumber = gbNumber / FORMAT;
        return new DecimalFormat("#.##TB").format(tbNumber);
    }

    public static void main(String[] args) {
        while (true){
            try {
                SystemUtil systemUtil = new SystemUtil();
                systemUtil.getDiskStore();
                TimeUnit.SECONDS.sleep(5);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Data
    public class CpuInfo{
        private int coreNum;  //cpu核数
        private String systemUsage; //cpu系统使用率
        private String userUsage; //cpu用户使用率
        private String waitRate; //cpu当前等待率
        private String usageRate; //cpu当前使用率
    }
    @Data
    public class MemoryInfo{
        private String total; //总内存
        private String used; //使用
        private String acaliable; //剩余内存
        private String usageRate; //使用率
    }
    @Data
    public class JvmInfo{
        private String total; //jvm内存总量
        private String used; //jvm已使用内存
        private String acaliable; //jvm剩余内存
        private String usageRate; //jvm内存使用率
        private String version; //java版本
    }

    @Data
    public class DiskStoreInfo{
        private String total; //总量
        private String used; //已使用
        private String acaliable; //剩余
        private String usageRate; //使用率
    }

    @Data
    public class NetworkData {
        private BlockingQueue<String> xAxis = new ArrayBlockingQueue<>(8);
        private int[] yAxis = new int[]{0,5,10,15,20,25,30};
        private BlockingQueue<Double> series = new ArrayBlockingQueue<>(8);

    }
}
