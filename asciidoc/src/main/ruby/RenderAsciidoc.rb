#!/usr/bin/ruby

require_relative '../../../../asciidoc/asciidoctor/lib/asciidoctor'
require 'erb'

$template_dir = 'asciidoc/src/main/backend/slim/docbook45/'

def render_asciidoc(source, target, backend)
    Asciidoctor.render_file(
        source,
        :to_file => target,
        :mkdirs => true,
        :template_dir => $template_dir,
        :safe => 0,
        :header_footer => true,
        :compact => false,
        :attributes => {
            'toc' => '',
            'numbered' => '',
            'docinfo' => '',
            'backend' => backend,
            'doctype' => 'book',
            'experimental' => ''
        }
    )
end

def render_html(source, target)
  render_asciidoc(source, target, 'html5')
end

def render_docbook(source, target)
  render_asciidoc(source, target, 'docbook')
end

