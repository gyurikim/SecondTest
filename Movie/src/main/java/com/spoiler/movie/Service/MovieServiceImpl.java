package com.spoiler.movie.Service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spoiler.movie.Dao.MovieCommentDao;
import com.spoiler.movie.Dao.MovieDao;
import com.spoiler.movie.Dto.MovieCommentDto;
import com.spoiler.movie.Dto.MovieDto;

@Service
public class MovieServiceImpl implements MovieService{
	@Autowired
	private MovieDao dao;
	@Autowired
	private MovieCommentDao commentDao;
	
	@Override
	public void movieList(HttpServletRequest request) {
		List<MovieDto> list = dao.getList();
		request.setAttribute("list", list);
	}
	@Override
	public void getDetail(HttpServletRequest request) {
		//파라미터로 전달되는 글번호
		int num=Integer.parseInt(request.getParameter("num"));
		//MovieDto 객체 생성 (select 할때 필요한 정보를 담기 위해)
		MovieDto dto=new MovieDto();				
		//MovieDto 에 글번호도 담기
		dto.setNum(num);
		//글 정보를 얻어와서
		MovieDto dto2=dao.getData(num);
		//request 에 글정보를 담고 
		request.setAttribute("dto", dto2);
		//댓글 목록을 얻어와서 request 에 담아준다.
		List<MovieCommentDto> commentList=commentDao.getList(num);
		request.setAttribute("commentList", commentList);
	}
	//댓글 저장하는 메소드 
	@Override
	public void saveComment(HttpServletRequest request) {
		//댓글 작성자
		String writer=(String)request.getSession().getAttribute("id");
		//댓글의 그룹번호
		int ref_group=
			Integer.parseInt(request.getParameter("ref_group"));
		//댓글의 대상자 아이디
		String target_id=request.getParameter("target_id");
		//댓글의 내용
		String content=request.getParameter("content");
		//댓글 내에서의 그룹번호 (null 이면 원글의 댓글이다)
		String comment_group=request.getParameter("comment_group");
		//저장할 댓글의 primary key 값이 필요하다
		int seq = commentDao.getSequence();
		System.out.println(writer);
		System.out.println(ref_group);
		System.out.println(target_id);
		System.out.println(content);
		System.out.println(comment_group);
		System.out.println(seq);
		//댓글 정보를 Dto 에 담기
		MovieCommentDto dto=new MovieCommentDto();
		dto.setNum(seq);
		dto.setWriter(writer);
		dto.setTarget_id(target_id);
		dto.setContent(content);
		dto.setRef_group(ref_group);
		if(comment_group==null) {//원글의 댓글인 경우
			//댓글의 글번호가 댓글의 그룹 번호가 된다.
			dto.setComment_group(seq);
		}else {//댓글의 댓글인 경우
			//comment_group 번호가 댓글의 그룹번호가 된다.
			dto.setComment_group(Integer.parseInt(comment_group));
		}
		//댓글 정보를 DB 에 저장한다.
		commentDao.insert(dto);				
	}

	@Override
	public void deleteComment(int num) {
		commentDao.delete(num);
	}

	@Override
	public void updateComment(MovieCommentDto dto) {
		commentDao.update(dto);
	}	
}
