package hoxy.hLivv.service;

import hoxy.hLivv.dto.product.CollaboDto;
import hoxy.hLivv.entity.Collabo;
import hoxy.hLivv.repository.CollaboRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CollaboService {

    private final CollaboRepository collaboRepository;

    @Transactional
    public CollaboDto saveCollaboProduct(CollaboDto collaboDto) {
        var collabo = collaboRepository.save(collaboDto.toEntity());
        return collaboDto;
    }

    public CollaboDto getCollaboProductWith(Long id) {
        return collaboRepository.getReferenceById(id)
                .toDto();
    }

    public List<CollaboDto> getAllCollaboProduct() {
        return collaboRepository.findAll()
                .stream()
                .map(Collabo::toDto)
                .toList();
    }

    @Transactional
    public CollaboDto updateCollaboProduct(CollaboDto collaboDto) {
        return null;
    }

    @Transactional
    public void removeCollaboProduct() {

    }

}
