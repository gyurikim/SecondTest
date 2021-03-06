package com.spoiler.movie.Dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spoiler.movie.Dto.MovieDto;

@Repository
public class MovieDaoImpl implements MovieDao{
	@Autowired
	private SqlSession session;
	
	@Override
	public List<MovieDto> movieList() {
		return session.selectList("movie.getHomeList");
	}

	@Override
	public int getCount(MovieDto dto) {
		return session.selectOne("movie.getCount",dto);
	}

	@Override
	public MovieDto getData(int num) {
		
		return session.selectOne("movie.getData", num);
	}

	@Override
	public void initMovie() {
		session.delete("movie.initMovie");
		
	}
	@Override
	public void updateMovie(MovieDto dto) {
		session.insert("movie.updateMovie", dto);
	}
}
