package com.kotei.workflowengine.bootstrap;

import com.kotei.workflowengine.core.WorkflowMap;
import com.kotei.workflowengine.core.WorkflowMapCollection;
import com.kotei.workflowengine.core.impl.WorkflowMapImpl;
import com.kotei.workflowengine.enums.WorkflowNodeEnum;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/3/30
 * 修改历史：
 * 1. [2018/3/30]创建文件
 *
 * @author chunl
 */
public class WorkflowsConfigurationLoader {

    /**
     * xsd架构命名空间
     */
    public final String Namespace = "http://WorkflowEngine.org/WorkflowEngine-schema.xsd";

    private static Logger logger = Logger.getLogger(WorkflowsConfigurationLoader.class);

    // Element
    public final String Workflow = "workflow";

    public final String Nodes = "nodes";
    public final String Node = "node";

    public final String FinishedNode = "finished-node";

    public final String Graphs = "graphs";
    public final String Graph = "graph";

    public final String Next = "next";

    public final String Branch = "branch";

    // Attribute
    public final String MapID = "mapID";
    public final String Keep = "keep";
    public final String NodeID = "nodeID";
    public final String ClassType = "classType";
    public final String NodeType = "nodeType";
    public final String Name = "name";
    public final String BranchSelector = "branch-selector";

    // // 前缀
    // private final String Prefix = "wf";
    // 前缀
    @SuppressWarnings("unused")
    private final String Prefix = "wf";

    /**
     * 从配置信息中加载流程图
     *
     * @param config
     *            特定的配置信息，如配置文件的路径、配置信息字符串、配置节名称等
     * @return
     * @throws Exception
     */
    public WorkflowMapCollection loadFile(String config) throws Exception {
        Document document = loadAndVerifyDocument(config);
        WorkflowMapCollection mapCollection = analyzeDocument(document);
        return mapCollection;
    }

    /**
     * 加载并检查xml文档
     *
     * @param config
     *            xml文件所在的路径
     * @return
     */
    private Document loadAndVerifyDocument(String config) {
        // TODO 20170407检查xml文档是否符合
        try {
            Resource resource = new ClassPathResource("config/WorkflowEngine-schema.xsd");
            String xsdPath = resource.getFile().getPath();
            if (!validateXml(xsdPath, config)) {
                return null;
            }
			/*
			 * 实例化一个DocumentBuildFactory，一个document构造器构建的工厂，
			 * 顾名思义，我想大家都能猜到它的作用了吧。
			 */
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db;
            Document document = null;
            // 实例化一个DocumentBuilder，一个document构造器，用于生成Document文档
            db = dbf.newDocumentBuilder();
            // 通过构造器的parse()方法，将一个File对象生成相应的Document文档
            document = db.parse(new File(config));
            return document;
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("", e1);
        }
        return null;
    }

    // TODO 20170407检查xml文档是否符合
    private boolean validateXml(String xsdPath, String config) {
        try {
            // 建立schema工厂
            SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
            // 建立验证文档文件对象，利用此文件对象所封装的文件进行schema验证
            File schemaFile = new File(xsdPath);
            // 利用schema工厂，接收验证文档文件对象生成Schema对象
            Schema schema = schemaFactory.newSchema(schemaFile);
            // 通过Schema产生针对于此Schema的验证器，利用schenaFile进行验证
            Validator validator = schema.newValidator();
            // 得到验证的数据源
            Source source = new StreamSource(config);
            // 开始验证，成功输出success!!!，失败输出fail
            validator.validate(source);
            return true;
        } catch (Exception e) {
            logger.error("", e);
        }

        return false;
    }

    /**
     * 对xml配置文件进行分析，得到流程图集合
     *
     * @param document
     * @return
     * @throws Exception
     */
    private WorkflowMapCollection analyzeDocument(Document document) throws Exception {
        WorkflowMapCollection mapCollection = new WorkflowMapCollection();

        NodeList workFlowList = document.getElementsByTagName(Workflow);
        for (int i = 0; i < workFlowList.getLength(); i++) {
            org.w3c.dom.Node workFlow = workFlowList.item(i);
            String mapID = workFlow.getAttributes().getNamedItem(MapID).getNodeValue();

            boolean keep = Boolean.parseBoolean(workFlow.getAttributes().getNamedItem(Keep).getNodeValue());

            WorkflowMap map = new WorkflowMapImpl(mapID, keep);
            // nodes
            org.w3c.dom.Element nodeEl = (org.w3c.dom.Element) workFlowList.item(i);
            NodeList nodeList = nodeEl.getElementsByTagName(Node);
            for (int j = 0; j < nodeList.getLength(); j++) {
                // node
                NamedNodeMap namedNodeMap = nodeList.item(j).getAttributes();
                WorkflowNodeEnum nodeType = WorkflowNodeEnum.DEFAULT;
                if (namedNodeMap.getNamedItem(NodeType) != null) {
                    nodeType = getWorkFlowNodeType(namedNodeMap.getNamedItem(NodeType).getNodeValue());
                }
                String nodeID = namedNodeMap.getNamedItem(NodeID).getNodeValue();
                String classType = namedNodeMap.getNamedItem(ClassType).getNodeValue();
                try {
                    map.addNode(nodeID, GetClassType(classType), nodeType.getValue());
                } catch (Exception e) {
                    throw e;
                }
            }

            NodeList endList = nodeEl.getElementsByTagName(FinishedNode);
            if (null != endList && 0 != endList.getLength()) {
                NamedNodeMap namedNodeMap = endList.item(0).getAttributes();
                map.setFinishedNode(namedNodeMap.getNamedItem(NodeID).getNodeValue());
            }

            // graphs
            NodeList graphList = nodeEl.getElementsByTagName(Graph);
            for (int k = 0; k < graphList.getLength(); k++) {
                // graph
                org.w3c.dom.Element element = (org.w3c.dom.Element) graphList.item(k);
                NamedNodeMap graphNodeMap = element.getAttributes();
                String nodeID = graphNodeMap.getNamedItem(NodeID).getNodeValue();
                // 允许有孤立的流程节点（可能该节点只出现在某个长分支内）
                NodeList branchList = element.getElementsByTagName(Branch);
                if (branchList.getLength() > 0) {
                    // 有分支
                    for (int l = 0; l < branchList.getLength(); l++) {
                        org.w3c.dom.Element branchElement = (org.w3c.dom.Element) branchList.item(l);
                        NamedNodeMap branchNodeMap = branchElement.getAttributes();
                        String branchName = branchNodeMap.getNamedItem(Name).getNodeValue();
                        String selectorTypeName = graphNodeMap.getNamedItem(BranchSelector).getNodeValue();
                        List<String> nexts = new ArrayList<String>();
                        // next
                        NodeList nextList = branchElement.getElementsByTagName(Next);
                        for (int m = 0; m < nextList.getLength(); m++) {
                            String seq = nextList.item(m).getAttributes().getNamedItem(NodeID).getNodeValue();
                            // //按照配置文件中声明的顺序作为分支流转的节点顺序
                            nexts.add(seq);
                        }
                        // 弧名称如果不一样的话，图如何根据权值进行选择分支路线？//同一个分支的弧名必须相同！！
                        try {
                            map.addBranch(nodeID, GetClassType(selectorTypeName), branchName,
                                    nexts.toArray(new String[nexts.size()]));
                        } catch (Exception e) {
                            throw e;
                        }
                    }
                } else {
                    // 没有分支
                    org.w3c.dom.Node nextItem = graphNodeMap.getNamedItem(Next);
                    if (nextItem != null) {
                        String nextNodeID = nextItem.getNodeValue();
                        map.setNext(nodeID, nextNodeID);
                    }
                }
            }
            mapCollection.add(map);
        }
        return mapCollection;
    }

    /**
     * 获取字符串表示的类型的Type
     *
     * @param classType
     * @return
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("rawtypes")
    private Class GetClassType(String classType) throws ClassNotFoundException {
        try {
            return Class.forName(classType);
        } catch (ClassNotFoundException e) {
            throw e;
        }
    }

    // 每条弧都添加一个唯一ID，在流程表和历史表都添加字段Arc，用于记录流程流转时的上一个路线、历史路线
    // 那么就可以把流程流转的整个路线图记录下来，在长分支流转的时候，也就可以根据路线来找到下一条路线了

    // 有名字的边优先匹配；如果没有匹配到，则再选择没有名字的边；名字不同的边无法匹配。如果前两者无法匹配，则CannotFlow
    // 流程在分支节点所选择的分支的名称，应作为流程系统数据存储（InstanceData分为UserData和流程Data，流程Data无法被用户访问）
    // 或者加一个字段叫Route，来存储分支名称等信息，也可以为后续代码中动态改变留下扩展

    // 定义了branch，当流程定义改变时，评估要修改的代码量

    // 配了分支branch之后，每条分支中的边自动命名，而不用给每条边都命名(配置文件的语义更清晰)

    // -----------------
    // 下一步怎么走 + 下几步怎么走 = 各种分支情况都适应

    /**
     * 获得结点类型
     *
     * @param workNodeType
     * @return
     */
    public WorkflowNodeEnum getWorkFlowNodeType(String workNodeType) {
        WorkflowNodeEnum workFlowNodeType = WorkflowNodeEnum.DEFAULT;
        switch (workNodeType) {
            case "start":
                workFlowNodeType = WorkflowNodeEnum.START;
                break;
            case "end":
                workFlowNodeType = WorkflowNodeEnum.END;
                break;
            default:
                workFlowNodeType = WorkflowNodeEnum.DEFAULT;
                break;
        }
        return workFlowNodeType;
    }
}
