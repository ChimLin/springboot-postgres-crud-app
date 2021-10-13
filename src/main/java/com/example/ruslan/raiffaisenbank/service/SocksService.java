package com.example.ruslan.raiffaisenbank.service;


import com.example.ruslan.raiffaisenbank.entity.SocksEntity;
import com.example.ruslan.raiffaisenbank.exception.NotEnoghParametersOrIncorrectInput;
import com.example.ruslan.raiffaisenbank.exception.QuantityLessZero;
import com.example.ruslan.raiffaisenbank.repository.SocksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SocksService {

    @Autowired
    private SocksRepository socksRepository;

    public SocksEntity add(SocksEntity socks)throws NotEnoghParametersOrIncorrectInput {
        isStringAndColor(socks.getColor());
        isWithinRangeCotton(socks.getCottonPart());
        isQuantityNotNegative(socks.getQuantity());

        SocksEntity oldEntity = checkForExistingEntity(socks);
        if(oldEntity == null){
            return socksRepository.save(socks);
        }

        socks.setId(oldEntity.getId());
        socks.setQuantity(socks.getQuantity() + oldEntity.getQuantity());
        return socksRepository.save(socks);
    }

    public SocksEntity substract(SocksEntity socks) throws NotEnoghParametersOrIncorrectInput, QuantityLessZero {
        isStringAndColor(socks.getColor());
        isWithinRangeCotton(socks.getCottonPart());
        isQuantityNotNegative(socks.getQuantity());

        SocksEntity oldEntity = checkForExistingEntity(socks);
        if(oldEntity == null){
            throw new  NotEnoghParametersOrIncorrectInput("не существуют такие носки");
        }

        if(oldEntity.getQuantity() - socks.getQuantity() < 0){
            throw new QuantityLessZero("Нельзя отнять от существуюшего количества носков");
        }

        socks.setId(oldEntity.getId());
        socks.setQuantity(oldEntity.getQuantity() - socks.getQuantity());
        return socksRepository.save(socks);
    }

    public String getEntities(String color, String operation, byte cottonPart) throws NotEnoghParametersOrIncorrectInput  {
        long quantitySocks = 0;
        isStringAndColor(color);
        isWithinRangeCotton(cottonPart);

        for (SocksEntity sock: socksRepository.findByColor(color)) {
            if(operation.equals("moreThan") && sock.getCottonPart() > cottonPart){
                quantitySocks += sock.getQuantity();
            }else if(operation.equals("lessThan") && sock.getCottonPart() < cottonPart){
                quantitySocks += sock.getQuantity();
            }
        }

        String word = operation.equals("moreThan") ? "более" : "менее";
        return String.format("общее количество %s носков с долей хлопка %s %d процентов состовляет %d", color, word, cottonPart, quantitySocks );

    }

    public SocksEntity checkForExistingEntity(SocksEntity socks){
        Iterable<SocksEntity> entities = socksRepository.findByColor(socks.getColor());

        if( entities == null) {
            return null;
        }

        for (SocksEntity sock: entities) {
            if(sock.getCottonPart() == socks.getCottonPart()){
                return sock;
            }
        }
        return null;
    }

    public void isStringAndColor(String color) throws NotEnoghParametersOrIncorrectInput {
        if(!(color instanceof String)){
            throw new NotEnoghParametersOrIncorrectInput("параметры запроса отсутствуют или имеют некорректный формат");
        }

        switch (color){
            case "red":
            case "orange":
            case "green":
            case "blue":
            case "purple":
            case "pink":
            case "silver":
            case "gold":
            case "beige":
            case "black":
            case "white":
            case "gray":
            case "grey": break;
            default: throw new NotEnoghParametersOrIncorrectInput("параметры запроса отсутствуют или имеют некорректный формат");
        }

    }

    public void isWithinRangeCotton(byte range) throws NotEnoghParametersOrIncorrectInput{
        if(range < 0 || range > 100){
            throw new NotEnoghParametersOrIncorrectInput("параметры запроса отсутствуют или имеют некорректный формат");
        }
    }

    public void isQuantityNotNegative(Long quantity) throws NotEnoghParametersOrIncorrectInput{
        if (quantity < 0){
            throw new NotEnoghParametersOrIncorrectInput("параметры запроса отсутствуют или имеют некорректный формат");
        }
    }



}
