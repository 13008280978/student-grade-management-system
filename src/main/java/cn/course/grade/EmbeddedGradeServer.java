package cn.course.grade;

import cn.course.grade.controller.AccountAction;
import cn.course.grade.controller.AlertAction;
import cn.course.grade.controller.AnalysisAction;
import cn.course.grade.controller.AuthAction;
import cn.course.grade.controller.CourseAction;
import cn.course.grade.controller.CreditAction;
import cn.course.grade.controller.HomeAction;
import cn.course.grade.controller.ProgramAction;
import cn.course.grade.controller.ScholarshipAction;
import cn.course.grade.controller.ScoreAction;
import cn.course.grade.controller.StudentAction;
import cn.course.grade.controller.TranscriptAction;
import org.apache.catalina.Context;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;

public class EmbeddedGradeServer {
    public static void main(String[] args) throws Exception {
        int port = Integer.parseInt(System.getProperty("app.port", "8081"));
        File base = new File(System.getProperty("java.io.tmpdir"), "grade-center-tomcat");
        File webapp = new File("src/main/webapp");

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);
        tomcat.setBaseDir(base.getAbsolutePath());
        tomcat.getConnector();

        Context context = tomcat.addWebapp("/grade-center", webapp.getAbsolutePath());
        context.setParentClassLoader(EmbeddedGradeServer.class.getClassLoader());
        WebResourceRoot resources = new StandardRoot(context);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
                new File("target/classes").getAbsolutePath(), "/"));
        context.setResources(resources);
        mapServlet(context, "auth", new AuthAction(), "/signin", "/signup", "/signout", "/language");
        mapServlet(context, "home", new HomeAction(), "/home");
        mapServlet(context, "accounts", new AccountAction(), "/accounts/*");
        mapServlet(context, "students", new StudentAction(), "/students/*");
        mapServlet(context, "courses", new CourseAction(), "/courses/*");
        mapServlet(context, "scores", new ScoreAction(), "/scores/*");
        mapServlet(context, "analysis", new AnalysisAction(), "/analysis");
        mapServlet(context, "transcript", new TranscriptAction(), "/transcript");
        mapServlet(context, "alerts", new AlertAction(), "/alerts");
        mapServlet(context, "scholarship", new ScholarshipAction(), "/scholarship");
        mapServlet(context, "credits", new CreditAction(), "/credits");
        mapServlet(context, "program", new ProgramAction(), "/program");

        tomcat.start();
        System.out.println("成绩系统已启动：http://localhost:" + port + "/grade-center");
        tomcat.getServer().await();
    }

    private static void mapServlet(Context context, String name,
                                   javax.servlet.http.HttpServlet servlet, String... patterns) {
        Tomcat.addServlet(context, name, servlet);
        for (String pattern : patterns) {
            context.addServletMappingDecoded(pattern, name);
        }
    }
}
