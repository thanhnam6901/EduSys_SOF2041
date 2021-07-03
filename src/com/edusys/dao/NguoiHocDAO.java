/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.edusys.dao;

import com.edusys.entity.NguoiHoc;
import com.edusys.utils.XJdbc;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ThanhNam
 */
public class NguoiHocDAO extends EduSysDAO<NguoiHoc, String> {

    String INSERT_SQL = "INSERT INTO NguoiHoc(MaNH, HoTen, GioiTinh, NgaySinh, DienThoai, Email, GhiChu, MaNV) VALUES(?,?,?,?,?,?,?,?)";
    String UPDATE_SQL = "UPDATE NguoiHoc SET HoTen = ?, GioiTinh = ?, NgaySinh = ?, DienThoai = ?, Email = ?, GhiChu = ?, MaNV = ? WHERE MaNH = ?";
    String DELETE_SQL = "DELETE FROM NguoiHoc WHERE MaNH = ?";
    String SELECT_ALL_SQL = "SELECT * FROM NguoiHoc";
    String SELECT_BY_ID_SQL = "SELECT * FROM NguoiHoc WHERE MaNH = ?";

    @Override
    public void insert(NguoiHoc entity) {
        XJdbc.update(INSERT_SQL, 
                entity.getMaNH(), entity.getHoTen(), entity.isGioiTinh(), 
                new java.sql.Date(entity.getNgaySinh().getTime()), 
                entity.getDienThoai(), entity.getEmail(), entity.getGhiChu(), entity.getMaNV());
    }

    @Override
    public void update(NguoiHoc entity) {
        XJdbc.update(UPDATE_SQL, 
                entity.getHoTen(), entity.isGioiTinh(), new java.sql.Date(entity.getNgaySinh().getTime()), 
                entity.getDienThoai(), entity.getEmail(), entity.getGhiChu(), entity.getMaNV(),
                entity.getMaNH());
    }

    @Override
    public void delete(String id) {
        XJdbc.update(DELETE_SQL, id);
    }

    @Override
    public List<NguoiHoc> selectAll() {
        return this.selectBySQL(SELECT_ALL_SQL);
    }

    @Override
    public NguoiHoc selectByID(String id) {
        List<NguoiHoc> list = this.selectBySQL(SELECT_BY_ID_SQL, id);
        if(list.isEmpty()){
            return null;
        }
        return list.get(0);
    }

    @Override
    protected List<NguoiHoc> selectBySQL(String sql, Object... args) {
        List<NguoiHoc> list = new ArrayList<NguoiHoc>();
        try {
            ResultSet rs = XJdbc.query(sql, args);
            while(rs.next()){
                NguoiHoc nh = new NguoiHoc();
                nh.setMaNH(rs.getString("MaNH"));
                nh.setHoTen(rs.getString("HoTen"));
                nh.setGioiTinh(rs.getBoolean("GioiTinh"));
                nh.setNgaySinh(rs.getDate("NgaySinh"));
                nh.setDienThoai(rs.getString("DienThoai"));
                nh.setEmail(rs.getString("Email"));
                nh.setGhiChu(rs.getString("GhiChu"));
                nh.setMaNV(rs.getString("MaNV"));
                nh.setNgayDK(rs.getDate("NgayDK"));
                list.add(nh);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public List<NguoiHoc> selectByKeyword(String keyword){
        String sql = "SELECT * FROM NguoiHoc WHERE HoTen LIKE ?";
        return this.selectBySQL(sql, "%" + keyword + "%");
    }

    public List<NguoiHoc> selectNotInCourse(int makh, String keyword){
        String sql = "SELECT * FROM NguoiHoc WHERE HoTen LIKE ? AND "
                + "MaNH NOT IN (SELECT MaNH FROM HocVien WHERE MaKH = ?)";
        return this.selectBySQL(sql, "%" + keyword + "%", makh);
    }
}
