package console;

import console.Bpmn2StampCli.OutputArguments;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * TODO not implemented yed
 */
public class ConsoleRunner {

	public static void main(String[] args) throws Exception {
		Configuration config = Configuration.getInstance();

		// conversion type/mode arguments
		ConverterType type = Bpmn2StampCli.parseTypeArguments(args);

		String baseIriArg = Bpmn2StampCli.parseBaseIriArguments(args);

		Bpmn2StampConverterService service = new Bpmn2StampConverterService(
				appendSuffix(baseIriArg, config.getBpmnSuffix()),
				appendSuffix(baseIriArg, config.getOrgSuffix()),
				appendSuffix(baseIriArg, config.getStampSuffix())
		);

		if (type == ConverterType.NONE) {
			Bpmn2StampCli.BpmnOrgAmArguments p = Bpmn2StampCli.parse1(args);
			OutputArguments outArgs = Bpmn2StampCli.parseOutputFileArguments(args, type, p.getBpmnFileArg());
			service.convertToStampAndBbo(
					new File(p.getBpmnFileArg()),
					new File(p.getOrgFileArg()),
					p.getActorMappingFilesList(),
					new File(outArgs.getOutputBboFile()),
					new File(outArgs.getOutputStampFile())
			);
		}
		if (type == ConverterType.BBO) {
			Bpmn2StampCli.BpmnOrgAmArguments p = Bpmn2StampCli.parse2(args);
			OutputArguments outArgs = Bpmn2StampCli.parseOutputFileArguments(args, type, p.getBpmnFileArg());
			service.convertToBbo(
					new File(p.getBpmnFileArg()),
					new File(p.getOrgFileArg()),
					p.getActorMappingFilesList(),
					new File(outArgs.getOutputFile())
			);
		} else if (type == ConverterType.STAMP) {
			Bpmn2StampCli.BpmnOrgAmArguments p = Bpmn2StampCli.parse2(args);
			OutputArguments outArgs = Bpmn2StampCli.parseOutputFileArguments(args, type, p.getBpmnFileArg());
			service.convertToStamp(
					new File(p.getBpmnFileArg()),
					new File(p.getOrgFileArg()),
					p.getActorMappingFilesList(),
					new File(outArgs.getOutputFile())
			);
		} else if (type == ConverterType.STAMP_FROM_BBO) {
			Bpmn2StampCli.StampFromBboArguments p = Bpmn2StampCli.parse3(args);
			OutputArguments outArgs = Bpmn2StampCli.parseOutputFileArguments(args, type, p.getBboFileArg());
			service.convertToStampFromBbo(
					new File(p.getBboFileArg()),
					new File(outArgs.getOutputFile())
			);
		}
		System.out.println("Done!");
	}

	private static String appendSuffix(String base, String suffix) {
		String pre = base;
		if (base.endsWith("/"))
			pre = base.substring(0, base.length() - 1);
		return pre + suffix;
	}

	enum ConverterType {
		NONE(null),
		BBO("bbo"),
		STAMP("stamp"),
		STAMP_FROM_BBO("stampFromBbo");

		private final String value;

		ConverterType(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public static ConverterType resolve(String value) {
			if (value == null) return NONE;
			for (ConverterType converterType : ConverterType.values()) {
				if (converterType.getValue() != null && converterType.getValue().equals(value))
					return converterType;
			}
			return null;
		}

		public static String strValues() {
			return Arrays.stream(ConverterType.values()).map(ConverterType::getValue).collect(Collectors.joining(","));
		}
	}
}