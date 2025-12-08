package com.experiment.parkingsystem.service.Impl;

import com.experiment.parkingsystem.common.PaginatedResponse;
import com.experiment.parkingsystem.dto.OwnerCreateRequest;
import com.experiment.parkingsystem.dto.OwnerResponse;
import com.experiment.parkingsystem.dto.OwnerUpdateRequest;
import com.experiment.parkingsystem.entity.Owner;
import com.experiment.parkingsystem.exception.ResourceNotFoundException;
import com.experiment.parkingsystem.mapper.OwnerMapper;
import com.experiment.parkingsystem.service.OwnerService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OwnerServiceImpl implements OwnerService {

    private final OwnerMapper ownerMapper;

    public OwnerServiceImpl(OwnerMapper ownerMapper) {
        this.ownerMapper = ownerMapper;
    }

    @Override
    public OwnerResponse createOwner(OwnerCreateRequest request) {
        Owner owner = new Owner();
        BeanUtils.copyProperties(request, owner);
        owner.setOwnerId(generateNewOwnerId());
        ownerMapper.insert(owner);
        return OwnerResponse.fromEntity(owner);
    }

    @Override
    public PaginatedResponse<OwnerResponse> getOwners(int page, int size, String name) {
        PageHelper.startPage(page, size);
        List<Owner> owners = ownerMapper.findAll(name);
        PageInfo<Owner> pageInfo = new PageInfo<>(owners);

        List<OwnerResponse> dtoList = pageInfo.getList().stream()
                .map(OwnerResponse::fromEntity)
                .collect(Collectors.toList());

        return new PaginatedResponse<>(pageInfo.getTotal(), dtoList);
    }

    @Override
    public OwnerResponse getOwnerById(String ownerId) {
        Owner owner = ownerMapper.findById(ownerId);
        if (owner == null) {
            throw new ResourceNotFoundException("Owner not found with id: " + ownerId);
        }
        return OwnerResponse.fromEntity(owner);
    }

    @Override
    public OwnerResponse updateOwner(String ownerId, OwnerUpdateRequest request) {
        Owner existingOwner = ownerMapper.findById(ownerId);
        if (existingOwner == null) {
            throw new ResourceNotFoundException("Owner not found with id: " + ownerId);
        }

        Owner ownerToUpdate = new Owner();
        BeanUtils.copyProperties(request, ownerToUpdate);
        ownerToUpdate.setOwnerId(ownerId);

        ownerMapper.update(ownerToUpdate);

        return OwnerResponse.fromEntity(ownerMapper.findById(ownerId));
    }

    @Override
    public void deleteOwner(String ownerId) {
        if (ownerMapper.existsById(ownerId) == 0) {
            throw new ResourceNotFoundException("Owner not found with id: ".concat(ownerId));
        }
        ownerMapper.deleteById(ownerId);
    }

    private synchronized String generateNewOwnerId() {
        String maxId = ownerMapper.findMaxOwnerId();

        // 1. 处理数据库为空的情况
        if (!StringUtils.hasText(maxId)) {
            return "O001";
        }

        // 2. 准备从 maxId 中提取数字
        String numberString;

        // 3. 兼容两种情况：'O001' 和 '1'
        if (maxId.toUpperCase().startsWith("O")) {
            // 如果是以 'O' 开头，截取掉第一个字符
            numberString = maxId.substring(1);
        } else {
            // 如果不以 'O' 开头 (例如就是 "1")，直接使用整个字符串
            numberString = maxId;
        }

        // 4. 将提取出的数字字符串转换为整数
        int number = Integer.parseInt(numberString);

        // 5. 返回格式化的、加1之后的新ID
        return "O" + String.format("%03d", number + 1);
    }
}