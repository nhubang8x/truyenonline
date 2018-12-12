package online.hthang.truyenonline.serviceImpl;

import online.hthang.truyenonline.repository.SeditorRepository;
import online.hthang.truyenonline.service.SeditorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Huy Thang
 */
@Service
public class SeditorServiceImpl implements SeditorService {

    @Autowired
    private SeditorRepository seditorRepository;
}
