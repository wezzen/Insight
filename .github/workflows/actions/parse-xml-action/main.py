import xml.etree.ElementTree as ET
import os
import sys
from pathlib import Path


def extract_test_coverage(directory_path):
    if not directory_path or not Path(directory_path).exists():
        raise ValueError(f"Directory not found: {directory_path}")

    _tests = 0
    _failures = 0
    _errors = 0
    _skipped = 0

    for xml_file in Path(directory_path).glob('*.xml'):
        tree = ET.parse(xml_file)
        root = tree.getroot()
        _tests += int(root.attrib['tests'])
        _failures += int(root.attrib['failures'])
        _errors += int(root.attrib['errors'])
        _skipped += int(root.attrib['skipped'])

    return _tests, _failures + _errors, _skipped, _tests - (_failures + _skipped + _errors)


if __name__ == '__main__':
    if len(sys.argv) != 2:
        print("Usage: python main.py <dir-path>")
        sys.exit(1)

    directory_path = sys.argv[1]
    tests, errors, skipped, passed = extract_test_coverage(directory_path)
    with open(os.environ['GITHUB_ENV'], 'a') as env_file:
        env_file.write(f"STATUS_TOTAL={tests}\n")
        env_file.write(f"STATUS_PASSED={passed}\n")
        env_file.write(f"STATUS_SKIPPED={skipped}\n")
        env_file.write(f"STATUS_ERRORS={errors}\n")