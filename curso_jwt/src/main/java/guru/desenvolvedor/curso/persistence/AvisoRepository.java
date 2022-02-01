package guru.desenvolvedor.curso.persistence;

import java.util.List;

import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.annotation.Secured;

@RepositoryRestResource(collectionResourceRel = "api", path = "api")
public interface AvisoRepository extends Repository<Aviso,Long>{
	
	@Secured("ROLE_AUTENTICADO")
	List<Aviso> findAll();
	
	
}
