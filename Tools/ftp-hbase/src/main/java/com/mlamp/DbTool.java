package com.mlamp;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DbTool {

    /**
     *查询对象集合
     */
    //rawTypes unchecked/
    @SuppressWarnings({"rawtypes","unchecked"})
    public static <T> List<T> getObjectList(Connection conn,String sql, Class<T> cls, String...args){
        List<T> list = new ArrayList<T>();
        Method[] ms = cls.getDeclaredMethods();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            ps = conn.prepareStatement(sql);
            if(args!=null){
                for(int i = 0; i < args.length;i++){
                    ps.setString(i+1, args[i]);
                }
            }
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int colCount = rsmd.getColumnCount();
            while(rs.next()){
                Object obj = cls.newInstance();
                for(int i = 1;i <=colCount; i++){
                    String colName = rsmd.getCatalogName(i);
                    for(int j= 0; j <= ms.length;j++){
                        Method method = ms[j];
                        Class[] cs = method.getParameterTypes();
                        if(method.getName().equalsIgnoreCase("set" + colName)){
                            if(cs[0] == int.class){
                                method.invoke(obj,rs.getInt(i));
                                break;
                            }
                            if(cs[0] == long.class){
                                method.invoke(obj,rs.getLong(i));
                                break;
                            }
                            if(cs[0] == Date.class){
                                method.invoke(obj,rs.getDate(i));
                                break;
                            }
                            if(cs[0] == String.class){
                                method.invoke(obj,rs.getString(i));
                                break;
                            }
                            if(cs[0] == double.class){
                                method.invoke(obj,rs.getDouble(i));
                                break;
                            }
                            if(cs[0] == boolean.class){
                                method.invoke(obj,rs.getBoolean(i));
                                break;
                            }
                        }
                    }
                }
                list.add((T) obj);
            }
        }catch (Exception e){
            System.out.println("SQL执行错误！" + sql);
        }finally{
            try{
                rs.close();
                ps.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return list;
    }

    public static List<String> queryListBySQL(Connection conn, String sql){
        List<String> list = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(rs.getString(1));
            }
        }catch(SQLException e){
            System.out.println("SQL执行错误！" + sql);
        }finally{
            try{
                rs.close();
                ps.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return list;
    }

    public static int queryIntBySQL(Connection conn, String sql){
        PreparedStatement ps = null;
        ResultSet rs = null;
        int column = 0;
        try{
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                column = rs.getInt(1);
            }
        }catch(SQLException e){
            System.out.println("SQL执行错误！" + sql);
        }finally{
            try{
                rs.close();
                ps.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return column;
    }

    /**
     * 执行DML（数据控制语句-insert/update/delete）语句
     */

    public static boolean updateBySQL(Connection conn, String sql){
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            ps = conn.prepareStatement(sql);
            int r = ps.executeUpdate();
            if(r<0){
                return false;
            }
        }catch(SQLException e){
            System.out.println("SQL执行错误！" + sql);
        }finally {
            try{
                rs.close();
                ps.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return true;
    }

    public static <T> void saveOrUpdateObjects(Connection conn, Class<T> cls, String tablename, List<?> list){
        if(list.size() == 0){
            return;
        }
        List<String> columns = new ArrayList<String>();
        Field[] allFiled = cls.getDeclaredFields();
        for(Field field:allFiled){
            columns.add(field.getName());
        }
        StringBuffer sb = new StringBuffer("insert into" + tablename + "values(");
        for(int i = 0; i < columns.size()-1;i++){
            sb.append("?,");
        }
        String sql = sb.append("?)").toString();
        Method[] ms = cls.getDeclaredMethods();
        List<Method> methods = new ArrayList<Method>();
        for(String col:columns){
            for(int j=0;j<ms.length;j++){
                Method method = ms[j];
                if(method.getName().equalsIgnoreCase("get" + col)){
                    methods.add(method);
                }
            }
        }
        PreparedStatement ps = null;
        try{
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(sql);
            for(Object obj:list){
                for(int i = 0; i<columns.size();i++){
                    ps.setObject(i+1,methods.get(i).invoke(obj));
                }
                ps.addBatch();
            }
            int[] arr = ps.executeBatch();
            conn.commit();
            System.out.println("Success Save Objects Num:" + arr.length);
        }catch (Exception e){
            System.out.println("SQL执行错误！" + sql);
        }finally {
            try{
                ps.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }
}






