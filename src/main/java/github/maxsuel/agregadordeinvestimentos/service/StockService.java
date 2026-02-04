package github.maxsuel.agregadordeinvestimentos.service;

import github.maxsuel.agregadordeinvestimentos.dto.CreateStockDto;
import github.maxsuel.agregadordeinvestimentos.entity.Stock;
import github.maxsuel.agregadordeinvestimentos.mapper.StockMapper;
import github.maxsuel.agregadordeinvestimentos.repository.StockRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    private final StockMapper stockMapper;

    @Transactional
    public void createStock(@NonNull CreateStockDto createStockDto) {
        var stock = stockMapper.toEntity(createStockDto);

        stockRepository.save(stock);
    }
}
