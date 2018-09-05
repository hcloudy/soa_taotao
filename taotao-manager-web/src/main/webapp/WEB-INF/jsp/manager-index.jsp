<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<td>
    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="importAll()">索引一键导入</a>
</td>
<script style="text/javascript">
    function importAll(){
        $.post("/index/import","",function(data){
            if(data.status == 200) {
                $.messager.alert("成功","索引导入成功");
            }else{
                $.messager.alert("失败","索引导入失败,请联系IT部门对接人!");
            }
        });
    }
</script>