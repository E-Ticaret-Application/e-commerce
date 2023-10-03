package rafa.ecommerce.core;

import org.modelmapper.ModelMapper;

public interface ModelMapperService {

	public ModelMapper forResponse();

	public ModelMapper forRequest();

}
