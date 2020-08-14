package com.lingxiao.blog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Main {
    static List<Org> list = new ArrayList<>();
    static {
        Org org = new Org();
        org.setId("1");
        org.setParentId("0");
        org.setName("aa");
        list.add(org);

        Org org1 = new Org();
        org1.setId("2");
        org1.setParentId("0");
        org1.setName("aa");
        list.add(org1);

        Org org2 = new Org();
        org2.setId("3");
        org2.setParentId("0");
        org2.setName("aa");
        list.add(org2);

        Org org3 = new Org();
        org3.setId("5");
        org3.setParentId("0");
        org3.setName("aa");
        list.add(org3);


        Org org4 = new Org();
        org4.setId("6");
        org4.setParentId("5");
        org4.setName("aa");
        list.add(org4);



        Org org5 = new Org();
        org5.setId("7");
        org5.setParentId("6");
        org5.setName("aa");
        list.add(org5);

        Org org6 = new Org();
        org6.setId("8");
        org6.setParentId("5");
        org6.setName("aa");
        list.add(org6);

        Org org7 = new Org();
        org7.setId("9");
        org7.setParentId("7");
        org7.setName("aa");
        list.add(org7);
    }
    public static void main(String[] args) {
        String str = null;
        String a = str + "aaa";
        Main main = new Main();

        long startTime = new Date().getTime();
        List<Org> childList = main.getChildList("5");
        long endTime = new Date().getTime();
        System.out.println("耗时："+ (endTime -startTime)/1000L + "秒");

        System.out.println(Arrays.toString(childList.toArray()));
    }


    public List<Org> getChildList(String root){
        List<Org> child = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Org org = list.get(i);
            if(root.equals(org.parentId)){
                child.add(org);
                List<Org> childList = getChildList(org.id);
                if (childList.size() == 0){
                    break;
                }
                child.addAll(childList);
            }
        }
        return child;
    }

    public static class Org{
        private String id;
        private String parentId;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Org{" +
                    "id='" + id + '\'' +
                    ", parentId='" + parentId + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}

