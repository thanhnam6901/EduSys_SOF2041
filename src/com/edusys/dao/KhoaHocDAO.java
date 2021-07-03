/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.edusys.dao;

import com.edusys.entity.KhoaHoc;
import com.edusys.utils.XJdbc;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ThanhNam
 */
public class KhoaHocDAO extends EduSysDAO<KhoaHoc, Integer> {

    String INSERT_SQL = "INSERT INTO KhoaHoc(MaCD, HocPhi, ThoiLuong, NgayKG, GhiChu, MaNV, NgayTao) VALUES(?,?,?,?,?,?,GETDATE())";
    String UPDATE_SQL = "UPDATE KhoaHoc SET MaCD = ?, HocPhi = ?, ThoiLuong = ?, NgayKG = ?, GhiChu = ?, MaNV = ? WHERE MaKH = ?";
    String DELETE_SQL = "DELETE FROM KhoaHoc WHERE MaKH = ?";
    String SELECT_ALL_SQL = "SELECT * FROM KhoaHoc";
    String SELECT_BY_ID_SQL = "SELECT * FROM KhoaHoc WHERE MaKH = ?";

    @Override
    public void insert(KhoaHoc entity) {
        XJdbc.update(INSERT_SQL,
                entity.getMaCD(), entity.getHocPhi(), entity.getThoiLuong(), 
                new java.sql.Date(entity.getNgayKG().getTime()), entity.getGhiChu(), entity.getMaNV());
    }

    @Override
    public void update(KhoaHoc entity) {
        XJdbc.update(UPDATE_SQL,
                entity.getMaCD(), entity.getHocPhi(), entity.getThoiLuong(), 
                new java.sql.Date(entity.getNgayKG().getTime()), entity.getGhiChu(), entity.getMaNV(),
                 entity.getMaKH());
    }

    @Override
    public List<KhoaHoc> selectAll() {
        return this.selectBySQL(SELECT_ALL_SQL);
    }

    @Override
    protected List<KhoaHoc> selectBySQL(String sql, Object... args) {
        List<KhoaHoc> list = new ArrayList<KhoaHoc>();
        try {
            ResultSet rs = XJdbc.query(sql, args);
            while (rs.next()) {
                KhoaHoc kh = new KhoaHoc();
                kh.setMaKH(rs.getInt("MaKH"));
                kh.setMaCD(rs.getString("MaCD"));
                kh.setHocPhi(rs.getDouble("HocPhi"));
                kh.setThoiLuong(rs.getInt("ThoiLuong"));
                kh.setNgayKG(rs.getDate("NgayKG"));
                kh.setMaNV(rs.getString("MaNV"));
                kh.setNgayTao(rs.getDate("NgayTao"));
                list.add(kh);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<KhoaHoc> selectByChuyenDe(String macd) {
        String sql = "SELECT * FROM KhoaHoc WHERE MaCD = ?";
        return this.selectBySQL(sql, macd);
    }

    @Override
    public void delete(Integer id) {
        XJdbc.update(DELETE_SQL, id);
    }

    @Override
    public KhoaHoc selectByID(Integer id) {
        List<KhoaHoc> list = this.selectBySQL(SELECT_BY_ID_SQL, id);
        if (list.isEmpty()) {
            return null;
        }
        return list.size() > 0 ? list.get(0) : null;
    }

    public List<Integer> selectYears(){
        String sql = "SELECT DISTINCT year(NgayKG) as YEAR FROM KhoaHoc ORDER BY YEAR DESC";
        List<Integer> list = new ArrayList<>();
        try {
            ResultSet rs = XJdbc.query(sql);
            while(rs.next()){
                list.add(rs.getInt(1));
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
