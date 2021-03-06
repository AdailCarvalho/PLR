package br.com.zipext.plr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.zipext.plr.model.PerfilUsuarioModel;
import br.com.zipext.plr.model.UsuarioModel;

@Repository
public interface PerfilUsuarioRepository extends JpaRepository<PerfilUsuarioModel, PerfilUsuarioModel.PerfilUsuarioPK> {
	
	@Modifying
	@Query("delete from PerfilUsuarioModel model "
		+  "where model.pk.usuario = :usuario")
	public void deleteByUsuario(@Param("usuario") UsuarioModel usuario);

	@Query("select model from PerfilUsuarioModel model"
		 + " where model.pk.usuario = :usuario"
		 + " and situacao = :situacao")
	public PerfilUsuarioModel findByUsuarioAndSituacao(@Param("usuario") UsuarioModel usuario, @Param("situacao") String situacao);
}
