package com.androidx.databinding;

import androidx.databinding.MergedDataBinderMapper;

public class DataBinderMapperImpl extends MergedDataBinderMapper {
  DataBinderMapperImpl() {
    addMapper(new com.apogee.geomaster.DataBinderMapperImpl());
  }
}
