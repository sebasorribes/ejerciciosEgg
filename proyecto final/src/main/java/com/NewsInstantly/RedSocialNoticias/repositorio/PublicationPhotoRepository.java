package com.NewsInstantly.RedSocialNoticias.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.NewsInstantly.RedSocialNoticias.entidades.ImagenNota;

@Repository
public interface PublicationPhotoRepository extends JpaRepository<ImagenNota, String>{

}
