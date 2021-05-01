package com.lingxiao.blog.utils;

import com.google.gson.Gson;
import com.lingxiao.blog.bean.statistics.LineChartData;
import com.lingxiao.blog.bean.statistics.Memory;
import com.lingxiao.blog.bean.statistics.Series;
import com.lingxiao.blog.bean.statistics.jvm.JvmHeapInfo;
import com.lingxiao.blog.bean.statistics.jvm.JvmInfo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;
import oshi.SystemInfo;
import oshi.hardware.*;
import oshi.software.os.OperatingSystem;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class SystemUtil {
    private static final SystemInfo SYSTEM_INFO = new SystemInfo();
    private final DecimalFormat mFormat = new DecimalFormat("#.##%");

    private CpuInfo mCpuInfo = null;

    /**
     * 这些不变的信息只需要获取一次
     */
    private void getBasicCpuInfo(){
        if (mCpuInfo != null) {
            return;
        }
        CentralProcessor processor = SYSTEM_INFO.getHardware().getProcessor();
        log.debug("cpu供应商: {}",processor.getVendor());
        log.debug("cpu名字: {}",processor.getName());
        log.debug("cpu负载: {}",processor.getSystemCpuLoad());
        log.debug("cpu核数:{}",processor.getLogicalProcessorCount());



        mCpuInfo = new CpuInfo();
        mCpuInfo.setVendor(processor.getVendor());
        mCpuInfo.setName(processor.getName());
        mCpuInfo.setCoreNum(processor.getLogicalProcessorCount());


    }

    public CpuInfo getCpuInfo() throws InterruptedException {
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
        log.debug("cpu系统使用率:{}",mFormat.format(cSys * 1.0 / totalCpu));
        log.debug("cpu用户使用率:{}",mFormat.format(user * 1.0 / totalCpu));
        log.debug("cpu当前等待率:{}",mFormat.format(iowait * 1.0 / totalCpu));
        log.debug("cpu当前使用率:{}",mFormat.format(1.0 - (idle * 1.0 / totalCpu)));

        getBasicCpuInfo();

        Sensors sensors = SYSTEM_INFO.getHardware().getSensors();
        double temperature = sensors.getCpuTemperature();
        double cpuVoltage = sensors.getCpuVoltage();
        int[] fanSpeeds = sensors.getFanSpeeds();
        log.debug("cpu温度: {} 摄氏度",temperature);
        log.debug("cpu电压: {} 伏",cpuVoltage);
        log.debug("cpu风扇速度: {}", Arrays.toString(fanSpeeds));
        mCpuInfo.setTemperature(temperature + "");
        mCpuInfo.setVoltage(cpuVoltage + "v");
        mCpuInfo.setFanSpeeds(Arrays.toString(fanSpeeds));

        mCpuInfo.setLoad(mFormat.format(processor.getSystemCpuLoad()));
        mCpuInfo.setSystemUsage(mFormat.format(cSys * 1.0 / totalCpu));
        mCpuInfo.setUserUsage(mFormat.format(user * 1.0 / totalCpu));
        mCpuInfo.setWaitRate(mFormat.format(iowait * 1.0 / totalCpu));
        mCpuInfo.setUsageRate(mFormat.format(1.0 - (idle * 1.0 / totalCpu)));
        return mCpuInfo;
    }

    public MemoryInfo getMemInfo() {
        log.debug("----------------主机内存信息----------------");
        GlobalMemory memory = SYSTEM_INFO.getHardware().getMemory();
        //总内存
        long totalByte = memory.getTotal();
        //剩余
        long acaliableByte = memory.getAvailable();

        log.debug("总内存 = {}" , formatByte(totalByte));
        log.debug("使用 = {}" , formatByte(totalByte - acaliableByte));
        log.debug("剩余内存 = {}" , formatByte(acaliableByte));
        log.debug("使用率 = {}" , mFormat.format((totalByte - acaliableByte) * 1.0 / totalByte));

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
        diskStoreInfo.setUsageRate(mFormat.format(freeSize*1.0/totalSize));
        return diskStoreInfo;
    }

    public OperatingSystem getOsInfo(){
        return SYSTEM_INFO.getOperatingSystem();
    }


    public JvmInfo getJvmInfo() {
        log.debug("----------------jvm信息----------------");
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
        log.debug("jvm内存总量 = {}" , formatByte(jvmTotalMemoryByte));
        log.debug("jvm已使用内存 = {}" , formatByte(jvmTotalMemoryByte - freeMemoryByte));
        log.debug("jvm剩余内存 = {}" , formatByte(freeMemoryByte));
        log.debug("jvm内存使用率 = {}" , mFormat.format((jvmTotalMemoryByte - freeMemoryByte) * 1.0 / jvmTotalMemoryByte));
        log.debug("java版本 = {}" , jdkVersion);
        log.debug("jdk安装路径 = {}",jdkHome);

        //非堆内存
        MemoryUsage nonHeapMemoryUsage = ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage();
        log.debug("jvm初始非堆内存: {}",formatByte(nonHeapMemoryUsage.getInit()));
        log.debug("jvm最大非堆内存: {}",formatByte(nonHeapMemoryUsage.getMax()));
        log.debug("jvm已用非堆内存: {}",formatByte(nonHeapMemoryUsage.getUsed()));


        JvmInfo jvmInfo = new JvmInfo();
        jvmInfo.setTotal(formatByte(jvmTotalMemoryByte));
        jvmInfo.setUsed(formatByte(jvmTotalMemoryByte - freeMemoryByte));
        jvmInfo.setAcaliable(formatByte(freeMemoryByte));
        jvmInfo.setUsageRate(mFormat.format((jvmTotalMemoryByte - freeMemoryByte) * 1.0 / jvmTotalMemoryByte));
        jvmInfo.setVersion(jdkVersion);
        jvmInfo.setJvmHeapInfo(getJvmHeapInfo());
        return jvmInfo;
    }

    private final LineChartData<String> heapLineChartData = new LineChartData<>();
    private BlockingQueue<String> usedHeapQueue;
    private BlockingQueue<String> canUseHeapQueue;
    private BlockingQueue<String> heapXAxisQueue;
    private List<Series<String>> heapSeries;
    private boolean initHeapQueued = false;
    private void initHeapQueue(){
        if (initHeapQueued){
            return;
        }
        usedHeapQueue = new ArrayBlockingQueue<>(5);
        canUseHeapQueue = new ArrayBlockingQueue<>(5);
        heapXAxisQueue = new ArrayBlockingQueue<>(5);
        Series<String> usedSeries = new Series<>();
        usedSeries.setName("已用堆内存");

        Series<String> canUseSeries = new Series<>();
        canUseSeries.setName("可用堆内存");
        heapSeries = new ArrayList<>();
        heapSeries.add(usedSeries);
        heapSeries.add(canUseSeries);
        initHeapQueued = true;
    }
    public JvmHeapInfo getJvmHeapInfo(){
        //获取堆内存信息
        MemoryUsage heapMemoryUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();

        Memory usedMemory = NumberUtil.formatByte(heapMemoryUsage.getUsed());
        Memory canUseMemory = NumberUtil.formatByte(heapMemoryUsage.getCommitted());
        String initMemory = formatByte(heapMemoryUsage.getInit());
        String maxMemory = formatByte(heapMemoryUsage.getMax());

        initHeapQueue();

        DateTime dateTime = new DateTime();
        String date = dateTime.toString("HH:mm:ss");
        addDataToQueue(heapXAxisQueue,5,date);
        addDataToQueue(usedHeapQueue,5,usedMemory.getValue());
        addDataToQueue(canUseHeapQueue,5,canUseMemory.getValue());
        heapLineChartData.setXAxis(heapXAxisQueue);
        heapSeries.get(0).setData(new ArrayList<>(usedHeapQueue));
        heapSeries.get(0).setUnit(StringUtils.substring(usedMemory.getUnit(),5));
        heapSeries.get(1).setData(new ArrayList<>(canUseHeapQueue));
        heapSeries.get(1).setUnit(StringUtils.substring(canUseMemory.getUnit(),5));
        heapLineChartData.setSeries(heapSeries);

        JvmHeapInfo jvmHeapInfo = new JvmHeapInfo();
        jvmHeapInfo.setInitMemory(initMemory);
        jvmHeapInfo.setMaxMemory(maxMemory);
        jvmHeapInfo.setHeapLineChartData(heapLineChartData);
        return jvmHeapInfo;
    }

    private void addDataToQueue(BlockingQueue<String> queue, int limit, String date){
        if (queue.size() >= limit){
            if (queue.poll() != null){
                queue.add(date);
            }
        }else {
            queue.add(date);
        }
    }

    public void getThread() {
        log.debug("----------------线程信息----------------");
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
            log.debug("线程数量：{}, 线程id：{}, 线程名称：{}, 线程状态：{}" , noThreads , thread.getId() , thread.getName() , thread.getState());
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




    public String[] getNearTime(){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = Integer.parseInt(String.valueOf(calendar.get(Calendar.SECOND) / 10).concat("0"));
        if (second >= 60){
            minute = minute + 1;
            second = 0;
        }
        if (minute >= 60){
            hour = hour + 1;
            minute = 0;
        }
        if (hour >= 24){
            hour = 0;
        }
        String[] nearTimeArray = new String[5];
        for (int i = 0; i < 5; i++) {
            StringBuilder builder = new StringBuilder();
            nearTimeArray[i] = builder.append(hour).append(":").append(minute).append(":").append(second).toString();
            second = second + 5;
            if (second >= 60){
                minute = minute + 1;
                second = 0;
            }
            if (minute >= 60){
                hour = hour + 1;
                minute = 0;
            }
            if (hour >= 24){
                hour = 0;
            }
        }
        return nearTimeArray;
    }

    public static void main(String[] args) {
        boolean tag = true;
        SystemUtil systemUtil = new SystemUtil();
        while (tag){
            try {
               /* systemUtil.getDiskStore();
                systemUtil.getJvmInfo();
                systemUtil.getThread();
                systemUtil.getCpuInfo();
                systemUtil.getOsInfo();*/
                systemUtil.getJvmHeapInfo();
                TimeUnit.SECONDS.sleep(5);
            }catch (Exception e){
                e.printStackTrace();
                tag = false;
            }
        }
    }

    @Data
    public static class CpuInfo{
        private int coreNum;  //cpu核数
        private String systemUsage; //cpu系统使用率
        private String userUsage; //cpu用户使用率
        private String waitRate; //cpu当前等待率
        private String usageRate; //cpu当前使用率
        private String vendor; //cpu供应商
        private String name; //cpu名字
        private String load; //cpu负载
        private String temperature;
        private String voltage;
        private String fanSpeeds;
    }
    @Data
    public static class MemoryInfo{
        private String total; //总内存
        private String used; //使用
        private String acaliable; //剩余内存
        private String usageRate; //使用率
    }

    @Data
    public static class DiskStoreInfo{
        private String total; //总量
        private String used; //已使用
        private String acaliable; //剩余
        private String usageRate; //使用率
    }

    @Data
    public static class NetworkData {
        private BlockingQueue<String> xAxis = new ArrayBlockingQueue<>(8);
        private int[] yAxis = new int[]{0,5,10,15,20,25,30};
        private BlockingQueue<Double> series = new ArrayBlockingQueue<>(8);

    }
}
