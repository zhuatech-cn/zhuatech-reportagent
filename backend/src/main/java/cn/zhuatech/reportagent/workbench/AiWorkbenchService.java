/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.reportagent.workbench;
import org.springframework.stereotype.Service;
import java.math.*;
import java.util.*;
import static cn.zhuatech.reportagent.workbench.WorkbenchInput.*;
/** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
@Service
public class AiWorkbenchService {
    /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
    public Map<String,Object> execute(Map<String,Object> r){
        validate(r);

        keys(r,"metrics");var items=rows(r,"metrics",1,200);Set<String> ids=new HashSet<>();List<Map<String,Object>> result=new ArrayList<>();int improved=0,deteriorated=0;
        for(var row:items){keys(row,"id","current","baseline","higherIsBetter");String id=id(row,"id");if(!ids.add(id))throw new IllegalArgumentException("指标编号重复");BigDecimal current=decimal(row,"current",-1e12,1e12),baseline=decimal(row,"baseline",-1e12,1e12),delta=current.subtract(baseline);boolean higher=flag(row,"higherIsBetter");int sign=delta.signum()*(higher?1:-1);if(sign>0)improved++;else if(sign<0)deteriorated++;
            Map<String,Object> value=new LinkedHashMap<>();value.put("id",id);value.put("current",current);value.put("baseline",baseline);value.put("absoluteChange",delta.stripTrailingZeros());value.put("direction",sign>0?"IMPROVED":sign<0?"DETERIORATED":"UNCHANGED");value.put("rateStatus",baseline.signum()==0?"ZERO_BASELINE":"CALCULATED");value.put("changePercent",baseline.signum()==0?null:delta.multiply(BigDecimal.valueOf(100)).divide(baseline.abs(),6,RoundingMode.HALF_UP).stripTrailingZeros());result.add(Collections.unmodifiableMap(value));
        }
        return Map.of("metrics",List.copyOf(result),"improved",improved,"deteriorated",deteriorated,"basis","CALLER_SUPPLIED_NUMERIC_VALUES");

    }
}
