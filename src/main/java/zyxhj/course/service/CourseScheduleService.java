package zyxhj.course.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.pool.DruidPooledConnection;

import zyxhj.course.domain.CourseSchedule;
import zyxhj.course.domain.attach.Classroom;
import zyxhj.course.repository.ClassroomRepository;
import zyxhj.course.repository.CourseScheduleRepository;
import zyxhj.utils.Singleton;

public class CourseScheduleService {
	private static Logger log = LoggerFactory.getLogger(CourseScheduleService.class);

	private CourseScheduleRepository courseScheduleRepository;
	private ClassroomRepository classroomRepository;

	public CourseScheduleService() {
		try {
			courseScheduleRepository = Singleton.ins(CourseScheduleRepository.class);
			classroomRepository = Singleton.ins(ClassroomRepository.class);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 添加课程安排记录
	 * 
	 * @param conn
	 * @param CSId
	 * @param termId
	 * @param courseId
	 * @param classroom
	 * @param classesIds
	 * @param teacherIds
	 * @param weekStart
	 * @param weekEnd
	 * @param numDay
	 * @param timeStart
	 * @param timeEnd
	 * @return
	 * @throws Exception
	 */
	public CourseSchedule addCourseSchedule(DruidPooledConnection conn, Long cSId, Long termId, Long courseId,
			Long classroomId, String classesIds, String teacherIds, Integer weekStart, Integer weekEnd, Integer numDay,
			Integer timeStart, Integer timeEnd) throws Exception {

		CourseSchedule courseSchedule = new CourseSchedule();
		courseSchedule.id = cSId;
		courseSchedule.termId = termId;
		courseSchedule.courseId = courseId;
		courseSchedule.classroomId = classroomId;
		/*
		 * String clasId = null; for (Long cla : classesIds) { clasId += cla + " "; }
		 */
		courseSchedule.classesIds = classesIds;
		/*
		 * String teaId = null; for (Long tea : teacherIds) { teaId += tea + " "; }
		 */
		courseSchedule.teacherIds = teacherIds;
		courseSchedule.weekStart = weekStart;
		courseSchedule.weekEnd = weekEnd;
		courseSchedule.numDay = numDay;
		courseSchedule.timeStart = timeStart;
		courseSchedule.timeEnd = timeEnd;
		if ((courseScheduleRepository.queryCourseSchedule(conn, classroomId, weekStart, weekEnd, numDay, timeStart,
				timeEnd)) != 1) {
			courseScheduleRepository.insert(conn, courseSchedule);
			return courseSchedule;
		} else {
			return null;
		}

	}



	/**
	 * 删除课程安排记录
	 * 
	 * @param conn
	 * @param cSId
	 * @throws Exception
	 */
	public void deleteCourseSchedule(DruidPooledConnection conn, Long cSId) throws Exception {
		courseScheduleRepository.deleteByKey(conn, "id", cSId);
	}

	/**
	 * 修改课程安排记录
	 * 
	 * @param conn
	 * @param cSId
	 * @param termId
	 * @param courseId
	 * @param classroom
	 * @param classesIds
	 * @param teacherIds
	 * @param weekStart
	 * @param weekEnd
	 * @param numDay
	 * @param timeStart
	 * @param timeEnd
	 * @throws Exception
	 */
	public CourseSchedule upCourseSchedule(DruidPooledConnection conn, Long cSId, Long termId, Long courseId,
			Long classroomId, String classesIds, String teacherIds, Integer weekStart, Integer weekEnd, Integer numDay,
			Integer timeStart, Integer timeEnd) throws Exception {

		CourseSchedule courseSchedule = new CourseSchedule();
		courseSchedule.id = cSId;
		courseSchedule.termId = termId;
		courseSchedule.courseId = courseId;
		courseSchedule.classroomId = classroomId;
		/*
		 * String clasId = null; for (Long cla : classesIds) { clasId += cla + " "; }
		 */
		courseSchedule.classesIds = classesIds;
		/*
		 * String teaId = null; for (Long tea : teacherIds) { teaId += tea + " "; }
		 */
		courseSchedule.teacherIds = teacherIds;
		courseSchedule.weekStart = weekStart;
		courseSchedule.weekEnd = weekEnd;
		courseSchedule.numDay = numDay;
		courseSchedule.timeStart = timeStart;
		courseSchedule.timeEnd = timeEnd;
		if ((courseScheduleRepository.queryCourseSchedule(conn, classroomId, weekStart, weekEnd, numDay, timeStart,
				timeEnd)) != 1) {
			courseScheduleRepository.updateByKey(conn, "id", cSId, courseSchedule, true);
			return courseSchedule;
		} else {
			return null;
		}
	}

	/**
	 * 查询课程安排记录
	 * 
	 * @param conns
	 * @param classroom
	 * @param weekStart
	 * @param weekEnd
	 * @param numDay
	 * @param timeStart
	 * @param timeEnd
	 * @return
	 * @throws Exception
	 */
	public int queryCourseSchedule(DruidPooledConnection conn, Long classroomId, Integer weekStart, Integer weekEnd,
			Integer numDay, Integer timeStart, Integer timeEnd) throws Exception {

		return courseScheduleRepository.queryCourseSchedule(conn, classroomId, weekStart, weekEnd, numDay, timeStart,
				timeEnd);
	}

	public CourseSchedule queryCourseScheduleById(DruidPooledConnection conn, Long cSId) throws Exception {
		return courseScheduleRepository.getByKey(conn, "id", cSId);
	}

	/**
	  * 获取楼号列表
	 * @param conn
	 * @return
	 * @throws Exception
	 */
	public List<Classroom> getBulidNum(DruidPooledConnection conn) throws Exception{
		return classroomRepository.getBulidNum(conn);
	}
	
	/**
	 * 根据楼号获取当前楼的所有教室
	 * @param conn
	 * @param bulidNum
	 * @return
	 * @throws Exception
	 */
	public List<Classroom> getClassroom(DruidPooledConnection conn, String bulidNum) throws Exception{
		return classroomRepository.getListByKey(conn, "bulid_num", bulidNum, 512, 0);
	}
	
	/**
	 * 查询当前教室课程安排
	 * 
	 * @param conn
	 * @param bulidNum 
	 * @param classroom
	 * @return
	 * @throws Exception
	 */
	public List<CourseSchedule> getCourseSchedule(DruidPooledConnection conn, String classroomName, String bulidNum) throws Exception {
		Long classroomId = classroomRepository.getClassroomId(conn,classroomName,bulidNum); //根据教室信息查询id
		return courseScheduleRepository.getListByKey(conn, "classroom_id", classroomId, 512, 0);
	}

}
