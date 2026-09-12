package com.rtravez.msc.service.common;

import com.rtravez.msc.exception.ExceptionManager;
import com.rtravez.msc.util.ProjectUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <b> Description de la class, interface o enumeration. </b>
 *
 * @author renetravez
 * @version $1.0$
 */
@Service
@Lazy
public class ValidationServiceImpl implements ValidationService {

	@Override
	@Transactional(readOnly = true)
	public boolean validationIdentification(String identification) throws ExceptionManager {
		return ProjectUtil.isCedulaValido(identification);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean validationRuc(String ruc) throws ExceptionManager {
		return ProjectUtil.isRucValido(ruc);
	}
}
