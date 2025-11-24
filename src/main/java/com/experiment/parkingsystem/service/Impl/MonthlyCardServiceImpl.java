package com.experiment.parkingsystem.service.Impl;

import com.experiment.parkingsystem.common.PaginatedResponse;
import com.experiment.parkingsystem.dto.*;
import com.experiment.parkingsystem.entity.MonthlyCard;
import com.experiment.parkingsystem.exception.ResourceNotFoundException;
import com.experiment.parkingsystem.mapper.MonthlyCardMapper;
import com.experiment.parkingsystem.service.MonthlyCardService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MonthlyCardServiceImpl implements MonthlyCardService {

    private final MonthlyCardMapper monthlyCardMapper;

    public MonthlyCardServiceImpl(MonthlyCardMapper monthlyCardMapper) {
        this.monthlyCardMapper = monthlyCardMapper;
    }

    /**
     * 办理新月卡
     * @param request 创建月卡的请求体
     * @return 创建成功后的月卡信息
     */
    @Override
    public MonthlyCardResponse createMonthlyCard(MonthlyCardCreateRequest request) {
        MonthlyCard card = new MonthlyCard();
        BeanUtils.copyProperties(request, card);
        card.setCardId(generateNewCardId());
        card.setStatus("正常"); // 初始状态为 "正常"
        card.setRenewTime(null); // 首次办理，没有续费时间

        monthlyCardMapper.insert(card);
        return MonthlyCardResponse.fromEntity(card);
    }

    /**
     * 续费月卡
     * @param cardId 要续费的月卡ID
     * @param request 续费请求体
     * @return 更新后的月卡信息
     */
    @Override
    public MonthlyCardResponse renewMonthlyCard(String cardId, MonthlyCardRenewRequest request) {
        // 1. 检查月卡是否存在
        findCardByIdOrThrow(cardId);

        // 2. 构造用于更新的实体对象
        MonthlyCard cardToUpdate = new MonthlyCard();
        cardToUpdate.setCardId(cardId);
        cardToUpdate.setValidityPeriod(request.getNewValidityPeriod());
        cardToUpdate.setAdminId(request.getAdminId());
        cardToUpdate.setRenewTime(new Date()); // 将当前时间设置为最近续费时间

        // 3. 执行更新
        monthlyCardMapper.update(cardToUpdate);

        // 4. 返回更新后的完整信息
        return MonthlyCardResponse.fromEntity(findCardByIdOrThrow(cardId));
    }

    /**
     * 更新月卡状态（挂失/解挂）
     * @param cardId 要操作的月卡ID
     * @param request 状态更新请求体
     * @return 更新后的月卡信息
     */
    @Override
    public MonthlyCardResponse updateMonthlyCardStatus(String cardId, MonthlyCardStatusRequest request) {
        // 1. 检查月卡是否存在
        findCardByIdOrThrow(cardId);

        // 2. 构造用于更新的实体对象
        MonthlyCard cardToUpdate = new MonthlyCard();
        cardToUpdate.setCardId(cardId);
        cardToUpdate.setStatus(request.getStatus());
        cardToUpdate.setAdminId(request.getAdminId());

        // 3. 执行更新
        monthlyCardMapper.update(cardToUpdate);

        // 4. 返回更新后的完整信息
        return MonthlyCardResponse.fromEntity(findCardByIdOrThrow(cardId));
    }

    /**
     * 分页查询月卡列表
     * @param page 页码
     * @param size 每页数量
     * @param vehicleId 车辆ID (可选过滤条件)
     * @param status 月卡状态 (可选过滤条件)
     * @return 分页的月卡列表
     */
    @Override
    public PaginatedResponse<MonthlyCardResponse> getMonthlyCards(int page, int size, String vehicleId, String status) {
        // 启动分页
        PageHelper.startPage(page, size);
        // 根据条件查询
        List<MonthlyCard> cards = monthlyCardMapper.findByCriteria(vehicleId, status);
        // 用 PageInfo 包装查询结果，以获取总数等分页信息
        PageInfo<MonthlyCard> pageInfo = new PageInfo<>(cards);

        // 将实体列表转换为 DTO 列表
        List<MonthlyCardResponse> dtoList = pageInfo.getList().stream()
                .map(MonthlyCardResponse::fromEntity)
                .collect(Collectors.toList());

        // 构造并返回分页响应对象
        return new PaginatedResponse<>(pageInfo.getTotal(), dtoList);
    }

    /**
     * 内部辅助方法：根据ID查找月卡，如果不存在则抛出异常
     * @param cardId 月卡ID
     * @return 查找到的月卡实体
     */
    private MonthlyCard findCardByIdOrThrow(String cardId) {
        MonthlyCard card = monthlyCardMapper.findById(cardId);
        if (card == null) {
            throw new ResourceNotFoundException("MonthlyCard not found with id: " + cardId);
        }
        return card;
    }

    /**
     * 内部辅助方法：生成新的月卡ID (例如 MC001, MC002...)
     * @return 新的唯一月卡ID
     */
    private synchronized String generateNewCardId() {
        String maxId = monthlyCardMapper.findMaxCardId();
        // 如果没有记录或ID格式不正确，则从第一个开始
        if (!StringUtils.hasText(maxId) || !maxId.startsWith("MC")) {
            return "MC001";
        }
        // 提取数字部分并加一
        int number = Integer.parseInt(maxId.substring(2));
        // 格式化为三位数的字符串，不足补零
        return "MC" + String.format("%03d", number + 1);
    }
}