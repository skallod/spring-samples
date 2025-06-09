package ru.galuzin.springrest.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.cache.Cache;
import ru.galuzin.springrest.dto.TestDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class MyCachedService {

    private final Cache cache;

    private final AtomicLong counter = new AtomicLong();


//    public List<TagDto> findAllByEntityId(@NonNull UUID entityId) {
//        return cache.get(entityId, () -> tagService.findAllByEntityId(entityId));
//    }
//

    public Map<UUID, List<TestDto>> findAllByEntityIds(List<UUID> entityIds) {
        boolean notFound = false;
        List notFoundList = new ArrayList<UUID>();
        Map<UUID, List<TestDto>> map = new HashMap<>();
        for (UUID entityId : entityIds) {
            List<TestDto> dtos = (List<TestDto>) Optional.ofNullable(cache.get(entityId))
                    .map(Cache.ValueWrapper::get).orElse(null);
            if (dtos == null) {
                // элемент в кеше не найден
//                notFound = true;
                // может нарушаться порядок если он есть (order by name)
                notFoundList.add(entityId);
                log.info("Not found in cache, entity with id [{}]", entityId);
//                break;
                continue;
            }
            map.put(entityId, dtos);
        }
        if (!notFoundList.isEmpty()) {
            log.info("Load cache from DB findAllByEntityIds [{}]", notFoundList);
            Map<UUID, List<TestDto>> mapFromDb = findAllByIdsViaDB(notFoundList);
            mapFromDb.entrySet().forEach(entry -> cache.put(entry.getKey(), entry.getValue()));
            map.putAll(mapFromDb);
        }
        log.info("Result map {}", map);
        return map;
//        return entityIds.stream().map(id -> Pair.of(id, findAllByEntityId(id))).collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    private Map<UUID, List<TestDto>> findAllByIdsViaDB(List<UUID> entityIds) {
        log.info("DB repo invoked");
        Map<UUID, List<TestDto>> map =new HashMap<>();
        for (UUID entityId : entityIds) {
            long l = counter.incrementAndGet();
            List<TestDto> dtos = List.of(
                    new TestDto("a" + counter), new TestDto("b" + counter)
            );
            map.put(entityId, dtos);
        }
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return map;
    }
}
